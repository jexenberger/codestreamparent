package io.codestream.di.api

import io.codestream.di.runtime.PropertyInjection
import io.codestream.util.mutableProperties


class ComponentDefinition<T>(val factory: Factory<T>,
                          val id: ComponentId,
                          val scopeType: String = ScopeType.singleton.name) {


    @Synchronized
    fun instance(ctx: Context): T {
        @Suppress("UNCHECKED_CAST")
        val scope = Scopes[scopeType]
                ?: throw ComponentDefinitionException(id, "defined in non-existent scope $scopeType")
        val (instance, isNew) = scope.getOrCreate(id, factory, ctx)
        if (isNew && factory.postBinding()) {
            bind(ctx, instance as Any)
        }
        return instance
    }

    private fun bind(ctx: Context, instance: Any) {
        instance::class.mutableProperties
                .map { PropertyInjection(id, it, instance) }
                .forEach { it.run(ctx) }
    }

}