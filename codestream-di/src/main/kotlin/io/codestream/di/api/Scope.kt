package io.codestream.di.api

interface Scope {

    val id: String

    fun <T> getOrCreate(id: ComponentId, factory: Factory<T>, ctx: Context): Pair<T,Boolean> {
        @Suppress("UNCHECKED_CAST")
        val instance = get(id, ctx) as T
        return instance?.let {
            return it to false
        } ?: run {
            val newInstance:T = factory.get(id, ctx)
            val instanceToUse = if (newInstance is Factory<*>) {
                newInstance.get(id, ctx) as T
            } else newInstance
            val ret = instanceToUse to true
            add(id, instanceToUse as Any)
            ret
        }

    }

    fun add(id: ComponentId, instance: Any)

    fun get(id: ComponentId, ctx: Context): Any?


}