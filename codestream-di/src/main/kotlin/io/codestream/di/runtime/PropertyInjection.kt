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

import io.codestream.di.api.ComponentId
import io.codestream.di.api.Context
import io.codestream.di.api.DependencyTarget
import kotlin.reflect.KMutableProperty

class PropertyInjection(val id: ComponentId, val property: KMutableProperty<*>, val instance: Any) {

    fun run(ctx: Context) {

        val type = instance::class
        val callSite = DependencyTarget(id, type, property)
        val dependency = DependencyResolver.getDependency(callSite, ctx)
        dependency?.let {
            val value = it.resolve<Any>(callSite, ctx)
            if (value == null && !callSite.isNullable) {
                throw PropertyInjectionException(type,callSite.name,"property is not nullable, but nullable value resolved")
            }
            try {
                property.setter.call(instance, value)
            } catch (e:IllegalArgumentException) {
                throw PropertyInjectionException(type, callSite.name, "Unable to set property -> '${property.name}' -> ${e.message}")
            }
        }
    }

}