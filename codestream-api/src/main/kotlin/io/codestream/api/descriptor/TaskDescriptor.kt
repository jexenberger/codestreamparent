package io.codestream.api.descriptor

import io.codestream.api.CodestreamModule
import io.codestream.api.Task
import io.codestream.api.TaskType
import io.codestream.di.api.Factory

data class TaskDescriptor(
        val module: CodestreamModule,
        val name: String,
        val description: String,
        val parameters: Map<String, ParameterDescriptor>,
        val factory: Factory<Task>,
        val groupTask:Boolean = false
) {

    val type: TaskType = TaskType(module.id, name)

    operator fun get(property:String) = parameters[property]

}