package io.codestream.runtime.container

import io.codestream.di.api.ComponentId
import io.codestream.di.api.Context
import io.codestream.di.api.Scope
import io.codestream.di.api.Scopes

object TaskScope : Scope {

    init {
        Scopes += this
    }

    override val id: String = "taskScope"

    val instances = mutableMapOf<io.codestream.runtime.container.TaskScopeId, Any>()

    @Synchronized
    override fun add(id: ComponentId, instance: Any) {
        instances[id as io.codestream.runtime.container.TaskScopeId] = instance
    }

    override fun get(id: ComponentId, ctx: Context) = instances[id]

    @Synchronized
    fun release(id: io.codestream.runtime.container.TaskScopeId) {
        instances.remove(id)
    }

    @Synchronized
    fun releaseAll() {
        instances.clear()
    }


}