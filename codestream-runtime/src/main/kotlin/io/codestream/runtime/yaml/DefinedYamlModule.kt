package io.codestream.runtime.yaml

import de.skuzzle.semantic.Version
import groovy.lang.GroovyClassLoader
import io.codestream.api.*
import io.codestream.api.descriptor.TaskDescriptor
import io.codestream.runtime.CompositeTask
import io.codestream.api.TaskId
import io.codestream.api.services.Language
import io.codestream.api.services.ScriptService
import io.codestream.doc.FunctionDoc
import io.codestream.doc.ParameterDoc
import io.codestream.doc.TaskDoc
import io.codestream.runtime.ModuleRegistry
import io.codestream.runtime.StreamContext
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileReader
import java.util.concurrent.ExecutorService
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class DefinedYamlModule(val path: File, val scriptingService: ScriptService) : BaseYamlModule {
    private val _tasks: MutableMap<TaskType, TaskDescriptor> = mutableMapOf()

    val descriptor: YamlModuleDescriptor

    override val tasks: Map<TaskType, TaskDescriptor> = _tasks

    override val description: String get() = descriptor.description
    override val name: String get() = descriptor.name ?: path.name
    override val version: Version get() = descriptor.version?.let { Version.parseVersion(it) } ?: defaultVersion
    override val scriptObject by lazy { loadScriptObject() }
    override val modulePath: String get() = path.absolutePath

    override val scriptObjectDocumentation: Collection<FunctionDoc>
        get() = scriptClass?.let { CodestreamModule.functionsDocumentationFromType(it, this) } ?: emptyList()

    private val scriptClass: KClass<*>?

    override fun get(name: TaskType) = _tasks[name]

    init {
        if (!path.isDirectory) {
            throw ModuleDoesNotExistException(path.name)
        }
        scriptClass = parseScriptClass()
        descriptor = load()
        loadTasks()
    }


    private fun loadScriptObject(): Any? {
        return scriptClass?.createInstance()
    }

    private fun parseScriptClass(): KClass<*>? {
        val path = File(path, "scripts/scriptObject.groovy")
        if (path.exists() && path.isFile) {
            return scriptingService.loadClass(path, this::class.java.classLoader, Language.groovy)
        }
        return null;
    }


    private fun load(): YamlModuleDescriptor {
        val load = Yaml().load(FileReader(File(path, "module.conf"))) as Map<String, Any?>
        return YamlModuleDescriptor(
                name = load["name"] as String?,
                description = load["description"] as String? ?: "<no description>",
                version = load["version"] as String?
        )
    }

    private fun loadTasks() {
        this.path.listFiles().forEach {
            if (it.isFile && it.name.endsWith("yaml")) {
                val descriptor = YamlTaskBuilder(it.nameWithoutExtension, this, it.readText()).taskDescriptor
                _tasks[descriptor.type] = descriptor
            }
        }
    }

    override fun getCompositeTask(id: TaskId, ctx: StreamContext): CompositeTask {
        val withThis = createScriptObjects()
        val taskDescriptor = getByName(id.taskType.name)
        taskDescriptor ?: throw TaskDoesNotExistException(id.taskType)
        val name = id.taskType.name
        val file = File(resolveTaskPath(name))
        val newContext = StreamContext(originatingContextId = ctx.originatingContextId)
        newContext.bindings["__modulePath"] = path.absolutePath
        val functionObjects = (ctx.bindings["_fn"] as MutableMap<String, Any>?)?.let { it } ?: mutableMapOf()
        functionObjects.putAll(createScriptObjects())
        newContext.bindings["_fn"] = functionObjects
        val task = CompositeTask(id, taskDescriptor, newContext)
        YamlTaskBuilder(name, this, file.readText()).defineTaskTree(task)
        return task
    }

    override fun resolveTaskPath(name: String) = "${this.path.absolutePath}/$name.yaml"

    override fun createScriptObjects(): MutableMap<String, Any> {
        val scriptObjects = dependencies
                .map { ModuleRegistry[it] ?: throw ModuleException(this, "has dependency on ${it} but does not exist") }
                .map { mod ->
                    mod.scriptObject?.let { "__${mod.name}" to it }
                }.filterNotNull().toMap()
        val withThis = mutableMapOf<String, Any>()
        withThis.putAll(scriptObjects)
        this.scriptObject?.let { withThis[this.name] = it }
        return withThis
    }


}