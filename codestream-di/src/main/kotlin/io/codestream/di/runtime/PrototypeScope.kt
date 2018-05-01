package io.codestream.di.runtime

import io.codestream.di.api.ComponentId
import io.codestream.di.api.Context
import io.codestream.di.api.Scope
import io.codestream.di.api.ScopeType

object PrototypeScope : Scope {

    override val id:String = ScopeType.prototype.name

    override fun get(id: ComponentId, ctx: Context): Any? {
        return null
    }

    override fun add(id: ComponentId, instance: Any) {
        //nothing to do here, prototypes will never be cached
    }
}