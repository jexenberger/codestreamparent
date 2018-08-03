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

import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Qualified
import io.codestream.di.api.*
import io.codestream.di.api.DependencyTarget
import kotlin.reflect.full.findAnnotation

class InjectionDependency : AnnotationDependency<Inject>(Inject::class, true, true) {

    override fun <T> resolve(annotation: Inject, target: DependencyTarget, ctx: Context): T? {
        val id = target.annotatedElement.findAnnotation<Qualified>()?.let {
            StringId(if (it.value.isNotEmpty()) it.value.trim() else target.name)
        } ?: TypeId(target.targetType)
        @Suppress("UNCHECKED_CAST")
        val instance:T? = ctx[id]
        return instance ?: throw UnsatisfiedDependencyInjection(target.name, "unable to resolve $id")
    }

}