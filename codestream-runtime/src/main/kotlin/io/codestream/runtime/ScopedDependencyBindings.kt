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

package io.codestream.runtime

import io.codestream.api.RunContext
import io.codestream.api.resources.WritableResourceRepository
import io.codestream.di.api.Context
import io.codestream.di.api.DependencyInjectionBindings

class ScopedDependencyBindings(
        ctx: Context,
        m: MutableMap<String, Any?> = mutableMapOf(),
        val parent: ScopedDependencyBindings? = null) : DependencyInjectionBindings(ctx, m), RunContext {


    init {
        put("_resources", ctx.get(WritableResourceRepository::class))
        put("_proxy", ctx.get(WritableResourceRepository::class))
    }

    override fun put(key: String, value: Any?) = m.put(key, value)


    fun subBindings(): ScopedDependencyBindings {
        return ScopedDependencyBindings(this.ctx, mutableMapOf(), this)
    }

    override fun get(key: String): Any? {
        return m.get(key) ?: parent?.get(key)
    }

    override fun containsValue(value: Any?): Boolean {
        if (!m.containsValue(value)) {
            return parent?.containsValue(value) ?: false
        }
        return true
    }

    override fun containsKey(key: String): Boolean {
        if (!m.containsKey(key)) {
            return parent?.containsKey(key) ?: false
        }
        return true
    }


    override fun isEmpty(): Boolean {
        return m.isEmpty() && parent?.isEmpty() ?: true
    }

    override val size: Int
        get() = super.size + (parent?.size ?: 0)

    override val entries: MutableSet<MutableMap.MutableEntry<String, Any?>>
        get() {
            val entries = mutableSetOf<MutableMap.MutableEntry<String, Any?>>()
            entries.addAll(parent?.entries ?: emptySet())
            entries.addAll(m.entries)
            return entries
        }
    override val keys: MutableSet<String>
        get() {
            val keys = mutableSetOf<String>()
            keys.addAll(parent?.keys ?: mutableSetOf())
            keys.addAll(super.keys)
            return keys
        }
    override val values: MutableCollection<Any?>
        get() {
            val values = mutableSetOf<Any?>()
            values.addAll(parent?.values ?: mutableSetOf())
            values.addAll(super.values)
            return values
        }

}