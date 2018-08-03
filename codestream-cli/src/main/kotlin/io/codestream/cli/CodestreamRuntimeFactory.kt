/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.codestream.cli

import io.codestream.api.Codestream
import io.codestream.api.CodestreamFactory
import io.codestream.api.CodestreamSettings
import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Qualified
import io.codestream.di.api.ComponentId
import io.codestream.di.api.Context
import io.codestream.di.api.Factory
import io.codestream.di.event.EventHandler
import io.codestream.runtime.CodestreamRuntime

class CodestreamRuntimeFactory(
        @Inject val codestreamSettings: CodestreamSettings,
        @Inject @Qualified("eventHandlers") val eventHandlers:Set<EventHandler<*>>

) : Factory<Codestream> {

    override fun get(id: ComponentId, ctx: Context) : Codestream {
        val get = CodestreamRuntime(codestreamSettings)
        get.eventHandlers.addAll(eventHandlers)
        return get
    }

}