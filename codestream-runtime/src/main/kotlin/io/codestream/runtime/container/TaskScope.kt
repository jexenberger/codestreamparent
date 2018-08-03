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