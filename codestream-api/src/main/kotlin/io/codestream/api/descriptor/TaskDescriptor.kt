package io.codestream.api.descriptor

import io.codestream.api.*
import io.codestream.api.metamodel.TaskDef
import io.codestream.di.api.Factory

data class TaskDescriptor(
        val module: CodestreamModule,
        val name: String,
        val description: String,
        val parameters: Map<String, ParameterDescriptor>,
        val factory: Factory<Task>,
        val groupTask:Boolean = false,
        val returnDescriptor: Pair<Type, String>? = null
) {

    val type: TaskType = TaskType(module.id, name)

    val requiredParameters:Map<String, ParameterDescriptor> get() = parameters.filter { (_, v) -> v.required }
    val patternParameters:Map<String, ParameterDescriptor> get() = parameters.filter { (_, v) -> v.regex.isNotEmpty() }

    operator fun get(property:String) = parameters[property]

    fun validate(defn: TaskDef) : ValidationErrors? {
        val errors = ValidationErrors()
        parameters.forEach { t, u ->
            u.isValid(defn.parameters[t])?.let { errors.merge(it) }
        }
        return errors.valid()
    }

}