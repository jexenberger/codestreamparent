package io.codestream.core.runtime.yaml

import de.skuzzle.semantic.Version
import io.codestream.core.api.CodestreamModule.Companion.defaultVersion
import io.codestream.core.api.TaskType
import io.codestream.core.api.descriptor.TaskDescriptor
import io.codestream.core.doc.FunctionDoc
import java.io.File
import java.util.*

class SingleFileModule(val file: File) : BaseYamlModule {
    override val modulePath: String = file.parentFile.absolutePath

    override val description: String get() = taskDescriptor.description
    override val name: String get() = file.parentFile.absolutePath
    override val version: Version get() = defaultVersion
    override val scriptObject = null
    override val scriptObjectDocumentation = emptyList<FunctionDoc>()


    val taskDescriptor: TaskDescriptor

    init {
        taskDescriptor = YamlTaskBuilder(file.absolutePath, this, file.readText()).load()
    }

    override fun createScriptObjects() = emptyMap<String, Any>()

    override fun resolveTaskPath(name: String) = file.absolutePath

    override val tasks: Map<TaskType, TaskDescriptor> = Collections.singletonMap(taskDescriptor.type, taskDescriptor)

    override fun get(name: TaskType) = taskDescriptor
}