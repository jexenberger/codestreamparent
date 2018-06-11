package io.codestream.di.runtime

import io.codestream.di.api.ComponentId
import io.codestream.di.api.Context
import io.codestream.di.api.Factory

class InstanceInjection<T>(val instance: T, val postBinding: Boolean = false) : Factory<T> {

    override fun postBinding(): Boolean = postBinding

    override fun get(id: ComponentId, ctx: Context): T {
        @Suppress("UNCHECKED_CAST")
        return instance as T
    }
}