package io.codestream.di.api

interface Scope {

    val id: String

    fun <T> getOrCreate(id: ComponentId, factory: Factory<T>, ctx: Context): Pair<T,Boolean> {
        @Suppress("UNCHECKED_CAST")
        val instance = get(id, ctx) as T
        return instance?.let {
            return it to false
        } ?: run {
            val newInstance:T = factory.get(ctx)
            val ret = newInstance to true
            add(id, newInstance as Any)
            ret
        }

    }

    fun add(id: ComponentId, instance: Any)

    fun get(id: ComponentId, ctx: Context): Any?


}