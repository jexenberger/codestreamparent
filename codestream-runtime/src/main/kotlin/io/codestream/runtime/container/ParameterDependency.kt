package io.codestream.runtime.container

import io.codestream.api.ComponentPropertyFailureException
import io.codestream.api.KotlinModule
import io.codestream.api.descriptor.ParameterDescriptor
import io.codestream.api.annotations.Parameter
import io.codestream.api.metamodel.TaskDef
import io.codestream.runtime.TaskDefId
import io.codestream.api.TaskId
import io.codestream.di.api.AnnotationDependency
import io.codestream.di.api.Context
import io.codestream.di.api.DependencyTarget
import io.codestream.util.Eval
import io.codestream.util.transformation.TransformerService
import kotlin.reflect.full.isSubclassOf

class ParameterDependency : AnnotationDependency<Parameter>(Parameter::class) {
    override fun <T> resolve(annotation: Parameter, target: DependencyTarget, ctx: Context): T {
        val id = target.id as TaskId

        val propertyName = if (annotation.alias.isNotEmpty()) annotation.alias else target.name

        val descriptor = KotlinModule.createProperty(target.name, target.targetType, annotation)

        val defnId = TaskDefId(id)
        val defn = ctx.get<TaskDef>(defnId)
                ?: throw ComponentPropertyFailureException(id.stringId, target.name, "task missing definition")
        val property = defn.parameters[propertyName]?.valueDefn

        if (property == null && annotation.default.isEmpty() && !target.isOptional) {
            throw ComponentPropertyFailureException(id.taskType.taskName, target.name, "not defined in definition, has no default and is not optional")
        }
        val valueDefn = property ?: annotation.default

        val eval = Eval.isScriptString(property.toString())
        return if (eval) {
            val value = Eval.eval<Any>(Eval.extractScriptString(valueDefn.toString()), ctx.bindings, Eval.engineByName("groovy"))
            checkValid(descriptor.type.convert<T>(value) as T, descriptor, id)
        } else if (target.targetType.isSubclassOf(Enum::class)) {
            checkValid(TransformerService.convertWithNull<Any>(valueDefn, target.targetType), descriptor, id) as T
        } else {
            checkValid(descriptor.type.convert<T>(valueDefn) as T, descriptor, id)
        }
    }

    fun <T> checkValid(value: T, descriptor: ParameterDescriptor, id: TaskId): T {
        descriptor.isValid(value)?.let { throw  ComponentPropertyFailureException(id.stringId, descriptor.name, it.toStringWithDescriptions()) }
        return value
    }
}