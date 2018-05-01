package io.codestream.di.api

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSuperclassOf

abstract class AnnotationDependency<A : Annotation>(
        val annotation: KClass<out Annotation>,
        val allowConstructors: Boolean = true,
        val allowProperties: Boolean = true) : Dependency {


    private fun supports(type: KAnnotatedElement, supported: Boolean): Boolean {
        if (!supported) {
            return false
        }
        return findAnnotation(type)?.let { true } ?: false
    }

    private fun findAnnotation(type: KAnnotatedElement) =
            type.annotations.find { annotation.isSuperclassOf(it::class) }

    override fun supports(type: KMutableProperty<*>): Boolean {
        return supports(type, allowProperties)
    }

    override fun supports(type: KParameter): Boolean {
        return supports(type, allowConstructors)
    }

    @Suppress("UNCHECKED_CAST")
    final override fun <T> resolve(target: DependencyTarget, ctx: Context): T {
        val targetAnnotation = findAnnotation(target.annotatedElement)!! as A
        return resolve(targetAnnotation, target, ctx)
    }

    abstract fun <T> resolve(annotation: A, target: DependencyTarget, ctx: Context): T
}