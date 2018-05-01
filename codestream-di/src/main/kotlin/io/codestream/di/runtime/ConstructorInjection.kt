package io.codestream.di.runtime

import io.codestream.di.annotation.WiredConstructor
import io.codestream.di.api.*
import io.codestream.util.noArgsConstructor
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

class ConstructorInjection<T>(val type: KClass<*>, val postBinding: Boolean = true) : Factory<T> {

    override fun postBinding(): Boolean = postBinding

    override fun get(ctx: Context): T {
        @Suppress("UNCHECKED_CAST")
        return run(ctx) as T
    }

    private fun resolveConstructor(): KFunction<*> {
        val constructor = type.constructors.find { it.findAnnotation<WiredConstructor>() != null }
        if (constructor != null) {
            return constructor
        }
        val noConstructors = type.constructors.size
        val candidate = if (noConstructors > 1) type.noArgsConstructor else type.constructors.iterator().next()
        return candidate
                ?: throw ConstructorInjectionException(type, "has more than one constructor or no no-args constructor")
    }


    fun run(ctx: Context): Any {
        val constructor = resolveConstructor()
        val parameters:Map<KParameter, Any?> = constructor.parameters.map {
            val target = DependencyTarget(it)
            val dependency = DependencyResolver.getDependency(target, ctx) ?: throw ConstructorInjectionException(type, "has more than one constructor or no no-args constructor")
             it to (dependency.resolve<Any>(target, ctx) as Any?)
        }.toMap()
        return constructor.callBy(parameters)!!
    }
}