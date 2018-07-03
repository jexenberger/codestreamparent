package io.codestream.core.runtime.yaml

import de.skuzzle.semantic.Version
import groovy.lang.GroovyClassLoader
import io.codestream.core.api.*
import io.codestream.core.runtime.CompositeTask
import io.codestream.core.runtime.TaskId
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileReader
import kotlin.reflect.full.createInstance

class YamlModule(val path: File) : CodestreamModule {

    private val taskTreeCacheMap: MutableMap<TaskType, CompositeTask> = mutableMapOf()
    private val _tasks: MutableMap<TaskType, TaskDescriptor> = mutableMapOf()

    val descriptor: YamlModuleDescriptor

    override val tasks: Map<TaskType, TaskDescriptor> = _tasks
    override val description: String get() = descriptor.description
    override val name: String get() = descriptor.name ?: path.name
    override val version: Version get() = Version.parseVersion(descriptor.version)
    override val scriptObject by lazy { loadScriptObject() }

    override fun get(name: TaskType) = _tasks[name]

    init {
        if (!path.isDirectory) {
            throw ModuleDoesNotExistException(path.name)
        }
        descriptor = load()
        loadTasks()
    }

    private fun loadScriptObject() : Any? {
        val path = File(path, "scripts/scriptObject.groovy")
        if (path.exists() && path.isFile) {
            return GroovyClassLoader().parseClass(path).kotlin.createInstance()
        }
        return null
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

    fun getCompositeTask(id: TaskId): CompositeTask {
        val withThis = createScriptObjects()
        val taskDescriptor = getByName(id.taskType.name)
        taskDescriptor ?: throw TaskDoesNotExistException(id.taskType)
        val name = id.taskType.name
        val file = File("${this.path.absolutePath}/$name.yaml")
        val task = CompositeTask(id, taskDescriptor, scriptObjects = withThis)
        YamlTaskBuilder(name, this, file.readText()).defineTaskTree(task)
        return task
    }

    private fun createScriptObjects(): MutableMap<String, Any> {
        val scriptObjects = dependencies.map { mod ->
            mod.module.scriptObject?.let { "__${mod.name}" to it }
        }.filterNotNull().toMap()
        val withThis = mutableMapOf<String, Any>()
        withThis.putAll(scriptObjects)
        this.scriptObject?.let { withThis["__${this.name}"] = it }
        return withThis
    }


}