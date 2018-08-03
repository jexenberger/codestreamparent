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

import io.codestream.di.runtime.PropertyInjection
import io.codestream.util.mutableProperties


class ComponentDefinition<T>(val factory: Factory<T>,
                          val id: ComponentId,
                          val scopeType: String = ScopeType.singleton.name) {


    @Suppress("UNCHECKED_CAST")
    @Synchronized
    fun instance(ctx: Context): T {
        val scope = Scopes[scopeType]
                ?: throw ComponentDefinitionException(id, "defined in non-existent scope $scopeType")
        val (instance, isNew) = scope.getOrCreate(id, factory, ctx)
        if (isNew && factory.postBinding()) {
            bind(ctx, instance as Any)
        }
        return instance
    }

    private fun bind(ctx: Context, instance: Any) {
        instance::class.mutableProperties
                .map { PropertyInjection(id, it, instance) }
                .forEach { it.run(ctx) }
    }

}