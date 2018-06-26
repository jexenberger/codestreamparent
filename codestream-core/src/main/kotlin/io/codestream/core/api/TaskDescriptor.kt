package io.codestream.core.api

import io.codestream.di.api.Factory

data class TaskDescriptor(
        val module: Module,
        val name: String,
        val description: String,
        val parameters: Map<String, ParameterDescriptor>,
        val factory: Factory<Task>,
        val groupTask:Boolean = false
) {

    val type:TaskType = TaskType(module.name, name)

    operator fun get(property:String) = parameters[property]

}