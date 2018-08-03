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

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.javaSetter
import kotlin.reflect.jvm.jvmErasure

data class DependencyTarget(val id:ComponentId,
                            val owner: KClass<*>,
                            val property: KMutableProperty<*>?,
                            val parameter: KParameter?) {

    constructor(id: ComponentId, owner: KClass<*>, property: KMutableProperty<*>) : this(id, owner, property, null)
    constructor(id: ComponentId, owner: KClass<*>, parameter: KParameter) : this(id, owner, null, parameter)

    init {
        if (property != null && parameter != null) {
            throw IllegalStateException("an either be property or parameter but not both")
        }
        if (property == null && parameter == null) {
            throw IllegalStateException("must define either parameter to property")
        }
    }

    val targetType: KClass<*> get() = perform({ it.returnType.jvmErasure }, { it.type.jvmErasure })

    val name: String get() = perform({ it.name }, { it.name!! })

    val path:String get() = "${owner.simpleName}.$name"

    val annotatedElement: KAnnotatedElement get() = perform({ it }, { it })

    val isNullable:Boolean get() {
        if (this.property != null) {
            return this.property.returnType.isMarkedNullable
        }
        if (this.parameter != null) {
            return this.parameter.type.isMarkedNullable
        }
        //should never get here
        throw IllegalStateException("an either be property or parameter but not both")
    }

    val isOptional:Boolean get() {
        if (this.property != null) {
            return false
        }
        if (this.parameter != null) {
            return this.parameter.isOptional
        }
        //should never get here
        throw IllegalStateException("an either be property or parameter but not both")
    }

    fun supports(dependency: Dependency): Boolean {
        return perform({ dependency.supports(it) }, { dependency.supports(it) })
    }

    private fun <T> perform(property: (p: KMutableProperty<*>) -> T, parameter: (p: KParameter) -> T): T {
        if (this.property != null) {
            return property(this.property)
        }
        if (this.parameter != null) {
            return parameter(this.parameter)
        }
        //should never get here
        throw IllegalStateException("an either be property or parameter but not both")
    }




}