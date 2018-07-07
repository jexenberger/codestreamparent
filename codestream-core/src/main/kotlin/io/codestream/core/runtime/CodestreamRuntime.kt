package io.codestream.core.runtime

import io.codestream.core.api.*
import io.codestream.core.api.metamodel.GroupTaskDef
import io.codestream.core.api.metamodel.ParameterDef
import io.codestream.core.api.metamodel.TaskDef
import io.codestream.core.runtime.task.GroupTaskHandler
import io.codestream.core.runtime.task.SimpleTaskHandler
import io.codestream.core.runtime.tree.Node
import io.codestream.core.runtime.yaml.DefinedYamlModule
import io.codestream.core.runtime.yaml.SingleFileModule
import io.codestream.di.api.addInstance
import java.io.File
import java.nio.file.Files
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CodestreamRuntime(settings: CodestreamSettings) : Codestream() {
    override val modules get() =  ModuleRegistry.modules


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
                ModuleRegistry += DefinedYamlModule(it.toFile())
            }
        }
        executorService = Executors.newFixedThreadPool(settings.systemThreads)
    }


    override fun runTask(moduleId: ModuleId, task: String, parameters: Map<String, Any?>, callback: ParameterCallback) : Map<String, Any?> {
        val module = ModuleRegistry[moduleId]
                ?: throw IllegalArgumentException("${moduleId} is not defined")
        val taskDescriptor = module[TaskType(module.name, task)]
                ?: throw IllegalArgumentException("Task '$task' does not exist on Module '${moduleId}'")

        val taskParams = taskDescriptor.parameters.map { (k, v) ->
            val paramValue = parameters[k] ?: callback.capture(v).orElse(null)
            k to ParameterDef(k, paramValue)
        }.toMap()
        val id = TaskId(taskDescriptor.type)
        val streamContext = StreamContext()
        streamContext.add(addInstance(executorService))
        val node: Node<StreamContext> = if (taskDescriptor.groupTask) {
            val defn = GroupTaskDef(id,taskParams, false)
            streamContext.registerTask(defn)
            GroupTaskHandler(id, false, defn)
        } else {
            val defn = TaskDef(id,taskParams)
            streamContext.registerTask(defn)
            SimpleTaskHandler(id, defn)
        }
        node.execute(streamContext)
        return streamContext.bindings
    }

    override fun runTask(module: ModuleId, task: String, parameters: Map<String, Any?>): Map<String, Any?> {
        return runTask(module, task, parameters, DefaultParameterCallback())
    }


    override fun runTask(file: File, parameters: Map<String, Any?>, callback: ParameterCallback) : Map<String, Any?> {
        val module = SingleFileModule(file)
        ModuleRegistry += module
        val descriptor = module.taskDescriptor
        return runTask(module.id, descriptor.name, parameters, callback)
    }
}