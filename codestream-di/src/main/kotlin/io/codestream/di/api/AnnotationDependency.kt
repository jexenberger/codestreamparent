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
import kotlin.reflect.full.isSuperclassOf

abstract class AnnotationDependency<A : Annotation>(
        val annotation: KClass<out Annotation>,
        val allowConstructors: Boolean = true,
        val allowProperties: Boolean = true) : Dependency {


    private fun supports(type: KAnnotatedElement, supported: Boolean): Boolean {
        if (!supported) {
            return false
        }
        return findAnnotation(type)?.let { true } ?: false
    }

    private fun findAnnotation(type: KAnnotatedElement) =
            type.annotations.find { annotation.isSuperclassOf(it::class) }

    override fun supports(type: KMutableProperty<*>): Boolean {
        return supports(type, allowProperties)
    }

    override fun supports(type: KParameter): Boolean {
        return supports(type, allowConstructors)
    }

    @Suppress("UNCHECKED_CAST")
    final override fun <T> resolve(target: DependencyTarget, ctx: Context): T? {
        val targetAnnotation = findAnnotation(target.annotatedElement)!! as A
        return resolve(targetAnnotation, target, ctx)
    }

    abstract fun <T> resolve(annotation: A, target: DependencyTarget, ctx: Context): T?
}