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

import io.codestream.di.annotation.WiredConstructor
import io.codestream.di.api.ComponentId
import io.codestream.di.api.Context
import io.codestream.di.api.DependencyTarget
import io.codestream.di.api.Factory
import io.codestream.util.noArgsConstructor
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

class ConstructorInjection<T>(val type: KClass<*>, val postBinding: Boolean = true) : Factory<T> {

    override fun postBinding(): Boolean = postBinding

    override fun get(id: ComponentId, ctx: Context): T {
        @Suppress("UNCHECKED_CAST")
        return run(id, ctx) as T
    }

    private fun resolveConstructor(): KFunction<*> {
        val constructor = type.constructors.find { it.findAnnotation<WiredConstructor>() != null }
        if (constructor != null) {
            return constructor
        }
        val noConstructors = type.constructors.size
        val candidate = if (noConstructors > 1) type.noArgsConstructor else type.constructors.iterator().next()
        return candidate
                ?: throw ConstructorInjectionException(type, "has more than one constructor or no no-args constructor")
    }


    fun run(id: ComponentId, ctx: Context): Any {
        val constructor = resolveConstructor()
        val parameters: Map<KParameter, Any?> = constructor.parameters.map {
            it to DependencyTarget(id, type, it)
        }.toMap().mapValues { (it, target) ->
            val dependency = DependencyResolver.getDependency(target, ctx)
                    ?: throw ConstructorInjectionException(type, "has more than one constructor or no no-args constructor")
            val resolve = dependency.resolve<Any>(target, ctx)
            it to (target to resolve)
        }.filterValues { (param, dep) ->
            val (target, value) = dep
            if (value != null) {
                return@filterValues true
            }
            if (!target.isOptional && !target.isNullable) {
                throw ConstructorInjectionException(target.targetType, "${target.name} resolve to no value but is not nullable or optional")
            }
            if (target.isOptional) {
                return@filterValues false
            }
            true
        }.map { it.key to it.value.second.second }.toMap()
        return try {
            constructor.callBy(parameters)!!
        } catch (e:IllegalArgumentException) {
            throw ConstructorInjectionException(type, "Unable to create instance of ${type.qualifiedName} with parameters $parameters")
        }
    }
}