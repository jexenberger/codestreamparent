package io.codestream.runtime

import io.codestream.api.CodestreamSettings
import io.codestream.api.*
import io.codestream.api.descriptor.ParameterDescriptor
import io.codestream.api.metamodel.GroupTaskDef
import io.codestream.api.metamodel.ParameterDef
import io.codestream.api.metamodel.TaskDef
import io.codestream.runtime.tree.Node
import io.codestream.di.api.addInstance
import io.codestream.doc.ModuleDoc
import io.codestream.runtime.task.GroupTaskHandler
import io.codestream.runtime.task.SimpleTaskHandler
import io.codestream.runtime.yaml.DefinedYamlModule
import io.codestream.runtime.yaml.SingleFileModule
import io.codestream.util.Ids
import java.io.File
import java.nio.file.Files
import java.util.concurrent.ExecutorService

class CodestreamRuntime(settings: CodestreamSettings) : Codestream() {
    override fun taskDoc(name: TaskType): TaskType? {
        return null
    }

    override val modules get() = io.codestream.runtime.ModuleRegistry.modules

    private val executorService: ExecutorService;


    init {
        if (!settings.yamlModulePath.exists()) {
            settings.yamlModulePath.mkdirs()
        }
        if (settings.yamlModulePath.isFile) {
            throw IllegalStateException("${settings.yamlModulePath} is a file")
        }
        Files.newDirectoryStream(settings.yamlModulePath.toPath()).forEach {
            if (it.toFile().isDirectory) {
                io.codestream.runtime.ModuleRegistry += DefinedYamlModule(it.toFile())
            }
        }
        executorService = settings.executor
    }

    fun resolveParameter(type: ParameterDescriptor, existingParameters: Map<String, Any?>, callback: ParameterCallback): Any? {
        val existing = existingParameters[type.name]
        return existing?.let { it } ?: if (type.default?.isNotBlank() ?: false) type.default else callback.capture(type)
    }

    override fun runTask(moduleId: ModuleId, task: String, parameters: Map<String, Any?>, callback: ParameterCallback): Map<String, Any?> {
        val module = ModuleRegistry[moduleId]
                ?: throw IllegalArgumentException("${moduleId} is not defined")
        val taskDescriptor = module[TaskType(module.id, task)]
                ?: throw IllegalArgumentException("Task '$task' does not exist on Module '${moduleId}'")

        val taskParams = taskDescriptor.parameters.map { (k, v) ->
            val paramValue = resolveParameter(v, existingParameters = parameters, callback = callback)
            k to ParameterDef(k, paramValue)
        }.toMap()
        val id = TaskId(taskDescriptor.type, Ids.next().toString())
        val streamContext = io.codestream.runtime.StreamContext()
        streamContext.add(addInstance(executorService))
        eventHandlers.forEach {
            streamContext.events.register(it)
        }
        val node: Node<io.codestream.runtime.StreamContext> = if (taskDescriptor.groupTask) {
            val defn = GroupTaskDef(id, taskParams, false)
            streamContext.registerTask(defn)
            GroupTaskHandler(id, false, defn)
        } else {
            val defn = TaskDef(id, taskParams)
            streamContext.registerTask(defn)
            SimpleTaskHandler(id, defn)
        }
        node.execute(streamContext)
        return streamContext.bindings
    }

    override fun runTask(module: ModuleId, task: String, parameters: Map<String, Any?>): Map<String, Any?> {
        return runTask(module, task, parameters, io.codestream.runtime.DefaultParameterCallback())
    }

    override fun runTask(file: File, parameters: Map<String, Any?>, callback: ParameterCallback): Map<String, Any?> {
        val module = SingleFileModule(file)
        io.codestream.runtime.ModuleRegistry += module
        val descriptor = module.taskDescriptor
        return runTask(module.id, descriptor.name, parameters, callback)
    }

    override fun moduleDoc(name: ModuleId): ModuleDoc? {
        return ModuleRegistry[name]?.documentation
    }
}
