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

import io.codestream.di.runtime.ConstructorInjection
import io.codestream.di.runtime.InstanceInjection


class ComponentDefinitionBuilder<T> {
    private var id: ComponentId? = null
    private var factory: Factory<T>? = null
    private var scope: String = ScopeType.singleton.name

    infix fun withId(id: ComponentId): ComponentDefinitionBuilder<T> {
        this.id = id
        return this
    }

    infix fun toFactory(factory: Factory<T>): ComponentDefinitionBuilder<T> {
        this.factory = factory
        return this
    }

    infix fun toScope(scope: String): ComponentDefinitionBuilder<T> {
        this.scope = scope
        return this
    }


    fun toDefn(): ComponentDefinition<T> {
        val candidateId = this.id ?: defaultId()
        val candidateFactory = this.factory ?: defaultFactory()
        candidateId ?: throw IllegalStateException("unable to infer id, define explicity")
        candidateFactory
                ?: throw IllegalStateException("unable to infer factory for -> '${candidateId.stringId}', define explicity")
        return ComponentDefinition(candidateFactory, candidateId, scope)
    }

    infix fun into(ctx: DefinableContext) {
        ctx.add(this)
    }

    private fun defaultFactory(): Factory<T>? {
        return id?.let {
            when (it) {
                is TypeId -> ConstructorInjection(it.type)
                else -> null
            }
        }
    }

    private fun defaultId(): ComponentId? {
        return factory?.let {
            when (it) {
                is ConstructorInjection -> TypeId(it.type)
                is InstanceInjection -> TypeId(it.instance as Any)
                else -> null
            }
        }
    }
}