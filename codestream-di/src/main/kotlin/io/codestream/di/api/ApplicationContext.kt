package io.codestream.di.api

import io.codestream.di.event.EventDispatcher
import io.codestream.di.runtime.InstanceScope
import io.codestream.di.runtime.PrototypeScope
import io.codestream.di.runtime.SingletonScope
import javax.script.Bindings

abstract class ApplicationContext(
        val ctx: MutableMap<ComponentId, ComponentDefinition<*>> = mutableMapOf(),
        override val values: MutableMap<String, Any> = mutableMapOf(),
        final override val events: EventDispatcher = EventDispatcher()) : DefinableContext {


    override val bindings: Bindings = DependencyInjectionBindings(this)

    companion object {
        init {
            synchronized(Scopes) {
                Scopes += InstanceScope
                Scopes += SingletonScope
                Scopes += PrototypeScope
            }
        }
    }

    init {
        addInstance(events) into this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> value(key: String): T? {
        return values[key] as T?
    }

    override fun setValue(key: String, value: Any) {
        values[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(id: ComponentId) = ctx[id]?.instance(this) as T?



    override fun <T> add(defn: () -> ComponentDefinitionBuilder<T>): ComponentDefinition<T> {
        val definitionBuilder = defn()
        definitionBuilder into this
        return definitionBuilder.toDefn()
    }

    override fun <T> add(defn: ComponentDefinitionBuilder<T>): ComponentDefinition<T> {
        val definition = defn.toDefn()
        ctx[definition.id] = definition
        return definition
    }

    override fun <T> addAll(defn: () -> Collection<ComponentDefinitionBuilder<T>>) {
        val defns = defn()
        for (definition in defns) {
            add(definition)
        }
    }
}