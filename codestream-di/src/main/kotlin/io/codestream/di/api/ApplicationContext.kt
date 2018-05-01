package io.codestream.di.api

import javax.script.Bindings

open abstract class ApplicationContext(
        val ctx: MutableMap<ComponentId, ComponentDefinition<*>> = mutableMapOf(),
        override val values: MutableMap<String, Any> = mutableMapOf()) : DefinableContext {


    @Suppress("UNCHECKED_CAST")
    override fun <T> value(key: String): T? {
        return values[key] as T?
    }

    override fun setValue(key: String, value: Any) {
        values[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(id: ComponentId) = ctx[id]?.instance(this) as T?

    override fun <T> eval(expr: String, bindings: Bindings): T? {
        return null
    }

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