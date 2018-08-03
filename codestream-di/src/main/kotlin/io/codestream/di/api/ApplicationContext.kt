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

import io.codestream.di.event.EventDispatcher
import io.codestream.di.runtime.DependencyResolver
import io.codestream.di.runtime.InstanceScope
import io.codestream.di.runtime.PrototypeScope
import io.codestream.di.runtime.SingletonScope
import javax.script.Bindings

abstract class ApplicationContext(
        val ctx: MutableMap<ComponentId, ComponentDefinition<*>> = mutableMapOf(),
        override val values: MutableMap<String, Any> = mutableMapOf(),
        final override val events: EventDispatcher = EventDispatcher()) : DefinableContext {


    override val bindings: Bindings = DependencyInjectionBindings(this)

    companion object {
        init {
            synchronized(Scopes) {
                Scopes += InstanceScope
                Scopes += SingletonScope
                Scopes += PrototypeScope
            }
        }
    }

    init {
        addInstance(events) into this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> value(key: String): T? {
        return values[key] as T?
    }

    override fun setValue(key: String, value: Any) {
        values[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(id: ComponentId) = ctx[id]?.instance(this) as T?



    override fun <T> add(defn: () -> ComponentDefinitionBuilder<T>): ComponentDefinition<T> {
        val definitionBuilder = defn()
        definitionBuilder into this
        return definitionBuilder.toDefn()
    }

    override fun <T> add(defn: ComponentDefinitionBuilder<T>): ComponentDefinition<T> {
        val definition = defn.toDefn()
        ctx[definition.id] = definition
        return definition
    }

    override fun <T> addAll(defn: () -> Collection<ComponentDefinitionBuilder<T>>) {
        val defns = defn()
        for (definition in defns) {
            add(definition)
        }
    }

    fun addDependencyResolver(resolver:Dependency) {
        DependencyResolver.add(resolver)
    }

    override fun hasComponent(id: ComponentId) = ctx.containsKey(id)
}