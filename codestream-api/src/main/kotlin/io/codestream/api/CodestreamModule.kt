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

package io.codestream.api

import de.skuzzle.semantic.Version
import io.codestream.api.annotations.ModuleFunction
import io.codestream.api.annotations.ModuleFunctionParameter
import io.codestream.api.descriptor.TaskDescriptor
import io.codestream.doc.FunctionDoc
import io.codestream.doc.ModuleDoc
import io.codestream.doc.ParameterDoc
import io.codestream.doc.TaskDoc
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation


val defaultVersion = Version.create(999999999, 999999999, 999999999)
val systemModuleId = ModuleId("sys", defaultVersion)

interface CodestreamModule {

    val name: String
    val description: String
    val version: Version

    val id: ModuleId
        get() = ModuleId(name, version)

    val scriptObjectDocumentation: Collection<FunctionDoc>

    val taskDocumentation: Collection<TaskDoc>
        get() {
            return tasks.map { (k, v) ->
                val paramDocs = v.parameters
                        .map { (name, parm) -> ParameterDoc(name, parm.description, parm.type.name, parm.required, parm.default, parm.regex) }
                        .toSet()
                        .sortedBy { it.name }
                TaskDoc(k.name, v.description, paramDocs, v.returnDescriptor?.let { (a, b) -> a.toString() to b })
            }.toSet().sortedBy { it.name }
        }

    val documentation: ModuleDoc get() = ModuleDoc(id, description, taskDocumentation, scriptObjectDocumentation)

    val scriptObject: Any?

    val tasks: Map<TaskType, TaskDescriptor>

    operator fun get(name: TaskType): TaskDescriptor?


    fun getByName(name: String) = tasks[TaskType(id, name)]

    val dependencies: Set<ModuleId> get() = emptySet()

    fun getDependencyVersion(moduleName: String): Version {
        return dependencies.find { it.name.equals(moduleName) }?.version ?: defaultVersion
    }


    companion object {

        fun versionString(version: Version) = if (version.equals(defaultVersion)) "LATEST" else version.toString()

        fun functionsDocumentationFromType(it: KClass<*>, module: CodestreamModule): List<FunctionDoc> {
            return it.declaredFunctions
                    .filter { it.visibility!!.equals(KVisibility.PUBLIC) }
                    .map {
                        val description = it.findAnnotation<ModuleFunction>()
                                ?: throw ModuleException(module, "Public Function '${it.name}' is not annotated with @ModuleFunction")
                        FunctionDoc(it.name, description.value, it.parameters.drop(1).map { p ->
                            val desc = p.findAnnotation<ModuleFunctionParameter>()
                                    ?: throw ModuleException(module, "Public Function '${it.name}.${p.name}' is not annotated with @ModuleFunctionParameter")
                            desc.value to desc.description
                        })
                    }
        }

    }
}