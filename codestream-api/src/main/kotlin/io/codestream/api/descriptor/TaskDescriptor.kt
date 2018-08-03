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

package io.codestream.api.descriptor

import io.codestream.api.*
import io.codestream.api.metamodel.TaskDef
import io.codestream.di.api.Factory

data class TaskDescriptor(
        val module: CodestreamModule,
        val name: String,
        val description: String,
        val parameters: Map<String, ParameterDescriptor>,
        val factory: Factory<Task>,
        val groupTask:Boolean = false,
        val returnDescriptor: Pair<Type, String>? = null
) {

    val type: TaskType = TaskType(module.id, name)

    val requiredParameters:Map<String, ParameterDescriptor> get() = parameters.filter { (_, v) -> v.required }
    val patternParameters:Map<String, ParameterDescriptor> get() = parameters.filter { (_, v) -> v.regex.isNotEmpty() }

    operator fun get(property:String) = parameters[property]

    fun validate(defn: TaskDef) : ValidationErrors? {
        val errors = ValidationErrors()
        parameters.forEach { t, u ->
            u.isValid(defn.parameters[t]).right?.let { errors.merge(it) }
        }
        return errors.valid()
    }

}