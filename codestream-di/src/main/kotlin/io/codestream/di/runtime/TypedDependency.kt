package io.codestream.di.runtime

import io.codestream.di.annotation.Qualified
import io.codestream.di.api.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

class TypedDependency(val allowProperties: Boolean = false) : Dependency {
    override fun supports(type: KParameter): Boolean {
        return true;
    }

    override fun supports(type: KMutableProperty<*>): Boolean {
        return allowProperties
    }

    override fun <T> resolve(target: DependencyTarget, ctx: Context): T? {
        val qualified = target.annotatedElement.findAnnotation<Qualified>()
        val id: ComponentId = qualified?.let {
            val id = if (it.value.isNotBlank()) it.value.trim() else target.name
            StringId(id)
        } ?: TypeId(target.targetType)
        @Suppress("UNCHECKED_CAST")
        val instance: T? = ctx[id]
        return instance
    }
}