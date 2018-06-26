package io.codestream.core.runtime.yaml

import com.fasterxml.jackson.module.kotlin.readValue
import de.skuzzle.semantic.Version
import io.codestream.core.api.ModuleDoesNotExistException
import io.codestream.core.api.TaskDescriptor
import io.codestream.core.api.TaskDoesNotExistException
import io.codestream.core.api.TaskType
import io.codestream.core.runtime.CodeStreamModule
import io.codestream.core.runtime.CompositeTask
import io.codestream.core.runtime.TaskId
import java.io.File

class YamlModule(val path: File) : CodeStreamModule {

    private val taskTreeCacheMap: MutableMap<TaskType, CompositeTask> = mutableMapOf()

    private val _tasks: MutableMap<TaskType, TaskDescriptor> = mutableMapOf()
    val descriptor: YamlModuleDescriptor

    override val tasks: Map<TaskType, TaskDescriptor> = _tasks
    override val name: String get() = descriptor.name ?: path.name

    override fun get(name: TaskType) = _tasks[name]

    override val description: String get() = descriptor.description

    override val version: Version get() = Version.parseVersion(descriptor.version)

    init {
        if (!path.isDirectory) {
            throw ModuleDoesNotExistException(path.name)
        }
        descriptor = load()
        loadTasks()
    }


    private fun load(): YamlModuleDescriptor {
        return yaml().readValue(File(path, "module.conf"))
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
        val taskDescriptor = getByName(id.taskType.name)
        taskDescriptor ?: throw TaskDoesNotExistException(id.taskType)
        val name = id.taskType.name
        val file = File("${this.path.absolutePath}/$name.yaml")
        val task = CompositeTask(id, taskDescriptor)
        YamlTaskBuilder(name, this, file.readText()).defineTaskTree(task)
        return task
    }


}