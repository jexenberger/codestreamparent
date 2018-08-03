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

package io.codestream.di.api

import io.codestream.util.OS
import javax.script.Bindings
import javax.script.SimpleBindings

open class DependencyInjectionBindings(val ctx: Context, protected val m: MutableMap<String, Any?> = mutableMapOf()) : AbstractMap<String, Any?>(), Bindings {

    override fun clear() = m.clear()

    override fun putAll(from: Map<out String, Any>) = m.putAll(from)

    override fun put(key: String, value: Any?) = m.put(key, value)

    override fun remove(key: String?) = m.remove(key)


    override val entries: MutableSet<MutableMap.MutableEntry<String, Any?>>
        get() = m.entries
    override val keys: MutableSet<String>
        get() = m.keys
    override val values: MutableCollection<Any?>
        get() = m.values

    init {
        put("_ctx", ctx)
        put("_os", OS.os())
    }

}