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

package io.codestream.di.runtime

import io.codestream.di.annotation.Qualified
import io.codestream.di.api.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

class TypedDependency(val allowProperties: Boolean = false) : Dependency {
    override fun supports(type: KParameter): Boolean {
        return true;
    }

    override fun supports(type: KMutableProperty<*>): Boolean {
        return allowProperties
    }

    override fun <T> resolve(target: DependencyTarget, ctx: Context): T? {
        val qualified = target.annotatedElement.findAnnotation<Qualified>()
        val id: ComponentId = qualified?.let {
            val id = if (it.value.isNotBlank()) it.value.trim() else target.name
            StringId(id)
        } ?: TypeId(target.targetType)
        @Suppress("UNCHECKED_CAST")
        val instance: T? = ctx[id]
        return instance
    }
}