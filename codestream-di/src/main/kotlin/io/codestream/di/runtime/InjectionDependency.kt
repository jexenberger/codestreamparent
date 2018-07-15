package io.codestream.di.runtime

import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Qualified
import io.codestream.di.api.*
import io.codestream.di.api.DependencyTarget
import kotlin.reflect.full.findAnnotation

class InjectionDependency : AnnotationDependency<Inject>(Inject::class, true, true) {

    override fun <T> resolve(annotation: Inject, target: DependencyTarget, ctx: Context): T? {
        val id = target.annotatedElement.findAnnotation<Qualified>()?.let {
            StringId(if (it.value.isNotEmpty()) it.value.trim() else target.name)
        } ?: TypeId(target.targetType)
        @Suppress("UNCHECKED_CAST")
        val instance:T? = ctx[id]
        return instance ?: throw UnsatisfiedDependencyInjection(target.name, "unable to resolve $id")
    }

}