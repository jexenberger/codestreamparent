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

package io.codestream.di.event

import io.codestream.util.ifTrue
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class EventDispatcher {

    private val handlers: MutableMap<KClass<*>, MutableList<EventHandler<*>>> = mutableMapOf()

    @Synchronized
    fun <T> register(handler: EventHandler<T>) {
        val type = ((handler::class.java.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0] as Class<*>)
        val kType = type.kotlin
        val handlersForType = handlers[kType] ?: run {
            val list = mutableListOf<EventHandler<*>>()
            handlers[kType] = list
            list
        }
        handlersForType += handler
    }

    fun <T : Any> publish(event: T) {
        handlers.forEach { (k, handler) ->
            val run = k.isInstance(event) || k.isSubclassOf(event::class)
            run.ifTrue {
                handler.forEach { (it as EventHandler<T>).onEvent(event) }
            }
        }
    }

}