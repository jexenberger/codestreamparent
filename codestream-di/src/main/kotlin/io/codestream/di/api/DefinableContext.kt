package io.codestream.di.api

interface DefinableContext : Context {
    fun <T> add(defn: ComponentDefinitionBuilder<T>): ComponentDefinition<T>
    fun <T> add(defn: () -> ComponentDefinitionBuilder<T>): ComponentDefinition<T>
    fun <T> addAll(defn: () -> Collection<ComponentDefinitionBuilder<T>>)
    fun setValue(key: String, value: Any)
}