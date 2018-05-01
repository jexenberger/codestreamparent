package io.codestream.di.runtime

import io.codestream.di.api.*

object InstanceScope : Scope{

    override val id:String = ScopeType.instance.name
    private val instances: MutableMap<ComponentId, Any> = mutableMapOf()

    override fun add(id: ComponentId, instance: Any) {
        instances[id] = instance
    }

    @Synchronized
    override fun get(id: ComponentId, ctx: Context): Any? {
        return instances[id]
    }

    fun clear() {
        instances.clear()
    }

}