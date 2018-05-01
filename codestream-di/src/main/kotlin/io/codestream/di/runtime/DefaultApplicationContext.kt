package io.codestream.di.runtime

import io.codestream.di.api.ApplicationContext
import io.codestream.di.api.ComponentDefinition
import io.codestream.di.api.ComponentId
import io.codestream.di.api.Scopes

class DefaultApplicationContext(ctx: MutableMap<ComponentId, ComponentDefinition<*>> = mutableMapOf(),
                                values: MutableMap<String, Any> = mutableMapOf()) : ApplicationContext(ctx, values) {

    companion object {
        init {
            synchronized(Scopes) {
                Scopes += InstanceScope
                Scopes += SingletonScope
                Scopes += PrototypeScope
            }
        }
    }
}