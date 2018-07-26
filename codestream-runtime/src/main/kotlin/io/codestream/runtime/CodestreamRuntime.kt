package io.codestream.runtime

import io.codestream.api.*
import io.codestream.api.descriptor.ParameterDescriptor
import io.codestream.api.metamodel.FunctionalTaskDef
import io.codestream.api.metamodel.GroupTaskDef
import io.codestream.api.metamodel.ParameterDef
import io.codestream.api.metamodel.TaskDef
import io.codestream.api.resources.ResolvingResourceRegistry
import io.codestream.api.resources.ResourceRepository
import io.codestream.api.resources.WritableResourceRepository
import io.codestream.util.script.ScriptService
import io.codestream.api.services.TemplateService
import io.codestream.di.api.TypeId
import io.codestream.di.api.addInstance
import io.codestream.doc.ModuleDoc
import io.codestream.runtime.resources.yaml.YamlResourceRepository
import io.codestream.runtime.services.CodestreamScriptingService
import io.codestream.runtime.services.MvelTemplateService
import io.codestream.runtime.task.GroupTaskHandler
import io.codestream.runtime.task.NonGroupTaskHandler
import io.codestream.runtime.tree.Node
import io.codestream.runtime.yaml.SingleFileModule
import io.codestream.runtime.yaml.YamlSecretStore
import io.codestream.util.Ids
import io.codestream.util.crypto.ResolvingSecretStore
import io.codestream.util.crypto.SimpleSecretStore
import io.codestream.util.crypto.SystemKey
import io.codestream.util.system
import java.io.File

class CodestreamRuntime(settings: CodestreamSettings) : Codestream() {
    override fun taskDoc(name: TaskType): TaskType? {
        return null
    }

    override val modules get() = io.codestream.runtime.ModuleRegistry.modules

    private val resources: WritableResourceRepository
    private val secretStore: SimpleSecretStore
    private val scriptService: ScriptService
    private val templateService: TemplateService
    init {
        scriptService = CodestreamScriptingService()
        ModuleRegistry.loadDefinedYamlModules(settings.yamlModulePath, scriptService)
        resources = ResolvingResourceRegistry(YamlResourceRepository("resources", settings.resourceRepositoryPath))
        secretStore = ResolvingSecretStore(YamlSecretStore(settings.secretStorePath))
        SystemKey.systemKeyPath = settings.globalKeyPath
        templateService = MvelTemplateService()
    }

    override fun shutdown() {
        system.optimizedExecutor.shutdownNow()
    }

    fun resolveParameter(type: ParameterDescriptor, existingParameters: Map<String, Any?>, callback: ParameterCallback): Any? {
        val existing = existingParameters[type.name]
        return existing?.let { it } ?: if (type.default?.isNotBlank() ?: false) type.default else callback.capture(type)
    }

    override fun runTask(moduleId: ModuleId, task: String, parameters: Map<String, Any?>, callback: ParameterCallback): Pair<Any, Map<String, Any?>> {
        val module = ModuleRegistry[moduleId]
                ?: throw IllegalArgumentException("${moduleId} is not defined")
        val taskDescriptor = module[TaskType(module.id, task)]
                ?: throw IllegalArgumentException("Task '$task' does not exist on Module '${moduleId}'")

        val taskParams = if (parameters.isEmpty()) {
            taskDescriptor.parameters.map { (k, v) ->
                val paramValue = resolveParameter(v, existingParameters = parameters, callback = callback)
                k to ParameterDef(k, paramValue)
            }.toMap()
        } else parameters.map { (k, v) -> k to ParameterDef(k, v) }.toMap()
        val id = TaskId(taskDescriptor.type, Ids.next().toString())
        val streamContext = createContext()
        val node: Node<io.codestream.runtime.StreamContext> = if (taskDescriptor.groupTask) {
            val defn = GroupTaskDef(id, taskParams, false)
            streamContext.registerTask(defn)
            GroupTaskHandler(id, false, defn)
        } else {
            val defn = if (taskDescriptor.returnDescriptor != null)
                FunctionalTaskDef(id, taskParams, {true}, "__result__")
            else
                TaskDef(id, taskParams)
            streamContext.registerTask(defn)
            NonGroupTaskHandler(id, defn)
        }
        node.execute(streamContext)
        val returnVal = streamContext.bindings["__result__"] ?: Unit
        return returnVal to streamContext.bindings
    }

    private fun createContext(): StreamContext {
        val streamContext = StreamContext()
        addInstance(secretStore) withId TypeId(SimpleSecretStore::class) into streamContext
        addInstance(scriptService) withId TypeId(ScriptService::class) into streamContext
        addInstance(resources) withId TypeId(ResourceRepository::class) into streamContext
        addInstance(templateService) withId TypeId(TemplateService::class) into streamContext
        val functionObjects = mutableMapOf<String, Any>()
        functionObjects.putAll(ModuleRegistry.systemModuleFunctionObjects)
        streamContext.bindings["_fn"] = functionObjects
        eventHandlers.forEach {
            streamContext.events.register(it)
        }
        return streamContext
    }

    override fun runTask(module: ModuleId, task: String, parameters: Map<String, Any?>): Pair<Any, Map<String, Any?>> {
        return runTask(module, task, parameters, io.codestream.runtime.DefaultParameterCallback())
    }

    override fun runTask(file: File, parameters: Map<String, Any?>, callback: ParameterCallback): Pair<Any, Map<String, Any?>> {
        val module = SingleFileModule(file)
        io.codestream.runtime.ModuleRegistry += module
        val descriptor = module.taskDescriptor
        return runTask(module.id, descriptor.name, parameters, callback)
    }

    override fun moduleDoc(name: ModuleId): ModuleDoc? {
        return ModuleRegistry[name]?.documentation
    }
}
