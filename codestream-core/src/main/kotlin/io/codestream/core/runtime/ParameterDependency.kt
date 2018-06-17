package io.codestream.core.runtime

import io.codestream.core.api.ComponentPropertyFailureException
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.createProperty
import io.codestream.core.metamodel.TaskDef
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
        val property = defn.parameters[propertyName]
                ?: throw ComponentPropertyFailureException(id.stringId, target.name, "not defined in definition")


        val valueDefn = property.valueDefn
        val eval = valueDefn?.let { Eval.isScriptString(it.toString()) } ?: false
        return if (eval && valueDefn != null) {
            val value = Eval.eval<Any>(Eval.extractScriptString(valueDefn.toString()), ctx.bindings)
            descriptor.type.convert<T>(value) as T
        } else
            descriptor.type.convert<T>(property.valueDefn) as T
    }
}