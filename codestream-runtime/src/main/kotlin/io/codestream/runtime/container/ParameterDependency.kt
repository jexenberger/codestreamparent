package io.codestream.runtime.container

import io.codestream.api.ComponentPropertyFailureException
import io.codestream.api.KotlinModule
import io.codestream.api.descriptor.ParameterDescriptor
import io.codestream.api.annotations.Parameter
import io.codestream.api.metamodel.TaskDef
import io.codestream.runtime.TaskDefId
import io.codestream.api.TaskId
import io.codestream.api.services.Language
import io.codestream.api.services.ScriptService
import io.codestream.di.api.AnnotationDependency
import io.codestream.di.api.Context
import io.codestream.di.api.DependencyTarget
import io.codestream.util.transformation.TransformerService
import kotlin.reflect.full.isSubclassOf

class ParameterDependency : AnnotationDependency<Parameter>(Parameter::class) {
    override fun <T> resolve(annotation: Parameter, target: DependencyTarget, ctx: Context): T? {

        val scriptService = ctx.get<ScriptService>(ScriptService::class)
            ?: throw IllegalStateException("No '${ScriptService::class}' registered in context")

        val id = target.id as TaskId

        val propertyName = if (annotation.alias.isNotEmpty()) annotation.alias else target.name

        val descriptor = KotlinModule.createProperty(target.name, target.targetType, annotation)

        val defnId = TaskDefId(id)
        val defn = ctx.get<TaskDef>(defnId)
                ?: throw ComponentPropertyFailureException(id.stringId, target.name, "task missing definition")
        val property = defn.parameters[propertyName]?.valueDefn

        if (property == null && annotation.default.isEmpty() && !target.isNullable) {
            throw ComponentPropertyFailureException(id.taskType.taskName, target.name, "not defined in definition, has no default and is not optional")
        }
        val valueDefn = property ?: annotation.default

        val result = scriptService.eval(property.toString(), ctx.bindings, true, Language.groovy) {
            if (target.targetType.isSubclassOf(Enum::class)) {
                TransformerService.convertWithNull<Any>(valueDefn, target.targetType)
            } else {
                descriptor.type.convert<T>(valueDefn)
            }
        }
        val postProcessed = postProcess(result, scriptService, ctx)
        return checkValid(postProcessed, descriptor, id) as T

    }

    fun <T> checkValid(value: T, descriptor: ParameterDescriptor, id: TaskId): T {
        descriptor.isValid(value)?.let { throw  it }
        return value
    }

    fun postProcessMap(value: Map<String, Any?>, scriptService: ScriptService, ctx: Context): Map<String, Any?> {
        return value.mapValues { (_, theValue) ->
            if (theValue is String) {
                scriptService.eval(theValue, ctx.bindings, true, Language.groovy) {
                    theValue
                }
            } else {
                theValue
            }
        }
    }

    fun <T> postProcess(value: T, scriptService: ScriptService, ctx: Context): T {
        return when (value) {
            is Map<*, *> -> postProcessMap(value as Map<String, Any?>, scriptService, ctx) as T
            else -> value
        }
    }
}