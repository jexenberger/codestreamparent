package io.codestream.di.api

import io.codestream.di.runtime.ConstructorInjection
import io.codestream.di.runtime.InstanceInjection


class ComponentDefinitionBuilder<T> {
    private var id: ComponentId? = null
    private var factory: Factory<T>? = null
    private var scope: String = ScopeType.singleton.name

    infix fun withId(id: ComponentId): ComponentDefinitionBuilder<T> {
        this.id = id
        return this
    }

    infix fun toFactory(factory: Factory<T>): ComponentDefinitionBuilder<T> {
        this.factory = factory
        return this
    }

    infix fun toScope(scope: String): ComponentDefinitionBuilder<T> {
        this.scope = scope
        return this
    }


    fun toDefn(): ComponentDefinition<T> {
        val candidateId = this.id ?: defaultId()
        val candidateFactory = this.factory ?: defaultFactory()
        candidateId ?: throw IllegalStateException("unable to infer id, define explicity")
        candidateFactory
                ?: throw IllegalStateException("unable to infer factory for -> '${candidateId.stringId}', define explicity")
        return ComponentDefinition(candidateFactory, candidateId, scope)
    }

    infix fun into(ctx: DefinableContext) {
        ctx.add(this)
    }

    private fun defaultFactory(): Factory<T>? {
        return id?.let {
            when (it) {
                is TypeId -> ConstructorInjection(it.type)
                else -> null
            }
        }
    }

    private fun defaultId(): ComponentId? {
        return factory?.let {
            when (it) {
                is ConstructorInjection -> TypeId(it.type)
                is InstanceInjection -> TypeId(it.instance as Any)
                else -> null
            }
        }
    }
}