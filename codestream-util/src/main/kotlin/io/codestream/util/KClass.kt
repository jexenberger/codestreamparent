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

package io.codestream.util

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure

val <T : Any> KClass<T>.mutableProperties: Collection<KMutableProperty<*>>
    get() = this.memberProperties.filter { it is KMutableProperty<*> }.map { it as KMutableProperty<*> }

fun <T : Any> KClass<T>.mutablePropertyByName(name: String) =
        this.memberProperties.filter { it is KMutableProperty<*> }.map { it as KMutableProperty<*> }.first { name.equals(it.name) }

val <T : Any> KClass<T>.noArgsConstructor: KFunction<T>?
    get() = this.constructors.find { it.parameters.isEmpty() }


fun <T : Any> KClass<T>.matchingConstructorByTypes(vararg types:KClass<*>) =
    this.constructors.find { it.parameters.foldRight(true, { parm, value -> value and types.contains(parm.type.jvmErasure)}) }

fun <T : Any> KClass<T>.matchingConstructorByNames(vararg name:String) =
    this.constructors.find { it.parameters.foldRight(true, { parm, value -> value and name.contains(parm.name)}) }
