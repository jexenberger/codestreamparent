package io.codestream.cli

import io.codestream.core.api.Codestream
import io.codestream.core.api.CodestreamSettings
import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Qualified
import io.codestream.di.api.ComponentId
import io.codestream.di.api.Context
import io.codestream.di.api.Factory
import io.codestream.di.event.EventHandler

class CodestreamRuntimeFactory(
        @Inject val codestreamSettings: CodestreamSettings,
        @Inject @Qualified("eventHandlers") val eventHandlers:Set<EventHandler<*>>

) : Factory<Codestream> {

    override fun get(id: ComponentId, ctx: Context) : Codestream {
        val get = Codestream.get(codestreamSettings)
        get.eventHandlers.addAll(eventHandlers)
        return get
    }

}