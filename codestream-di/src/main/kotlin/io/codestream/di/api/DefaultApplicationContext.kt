package io.codestream.di.api

import io.codestream.di.api.ApplicationContext
import io.codestream.di.api.ComponentDefinition
import io.codestream.di.api.ComponentId

class DefaultApplicationContext(ctx: MutableMap<ComponentId, ComponentDefinition<*>> = mutableMapOf(),
                                values: MutableMap<String, Any> = mutableMapOf()) : ApplicationContext(ctx, values) {


}