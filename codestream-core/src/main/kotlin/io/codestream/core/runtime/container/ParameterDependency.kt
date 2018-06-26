package io.codestream.core.runtime.container

import io.codestream.core.api.ComponentPropertyFailureException
import io.codestream.core.api.ParameterDescriptor
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.createProperty
import io.codestream.core.runtime.metamodel.TaskDef
import io.codestream.core.runtime.TaskDefId
import io.codestream.core.runtime.TaskId
import io.codestream.di.api.AnnotationDependency
import io.codestream.di.api.Context
import io.codestream.di.api.DependencyTarget
import io.codestream.util.Eval

class ParameterDependency : AnnotationDependency<Parameter>(Parameter::class) {
    override fun <T> resolve(annotation: Parameter, target: DependencyTarget, ctx: Context): T {
        val id = target.id as TaskId

        val propertyName = if (annotation.alias.isNotEmpty()) annotation.alias else target.name

        val descriptor = createProperty(target.name, target.targetType, annotation)

        val defnId = TaskDefId(id)
        val defn = ctx.get<TaskDef>(defnId)
                ?: throw ComponentPropertyFailureException(id.stringId, target.name, "task missing definition")
        val property = defn.parameters[propertyName]?.valueDefn

        if (property == null && annotation.default.isEmpty()  && !target.isOptional) {
            throw ComponentPropertyFailureException(id.stringId, target.name, "not defined in definition, has no default and is not optional")
        }
        val valueDefn = property ?: annotation.default

        val eval = Eval.isScriptString(property.toString())
        return if (eval) {
            val value = Eval.eval<Any>(Eval.extractScriptString(valueDefn.toString()), ctx.bindings)
            checkValid(descriptor.type.convert<T>(value) as T, descriptor, id)
        } else {
            checkValid(descriptor.type.convert<T>(valueDefn) as T, descriptor, id)
        }
    }

    fun <T> checkValid(value: T, descriptor: ParameterDescriptor, id: TaskId): T {
        descriptor.isValid(value)?.let { throw  ComponentPropertyFailureException(id.stringId, descriptor.name, it.toStringWithDescriptions()) }
        return value
    }
}