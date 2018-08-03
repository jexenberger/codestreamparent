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

package io.codestream.runtime.yaml

import de.skuzzle.semantic.Version
import io.codestream.api.TaskType
import io.codestream.api.defaultVersion
import io.codestream.api.descriptor.TaskDescriptor
import io.codestream.doc.FunctionDoc
import java.io.File
import java.util.*

class SingleFileModule(val file: File) : BaseYamlModule {
    override val modulePath: String = file.parentFile.absolutePath

    override val description: String get() = taskDescriptor.description
    override val name: String get() = file.parentFile.absolutePath
    override val version: Version get() = defaultVersion
    override val scriptObject = null
    override val scriptObjectDocumentation = emptyList<FunctionDoc>()


    val taskDescriptor: TaskDescriptor

    init {
        taskDescriptor = YamlTaskBuilder(file.absolutePath, this, file.readText()).load()
    }

    override fun createScriptObjects() = emptyMap<String, Any>()

    override fun resolveTaskPath(name: String) = file.absolutePath

    override val tasks: Map<TaskType, TaskDescriptor> = Collections.singletonMap(taskDescriptor.type, taskDescriptor)

    override fun get(name: TaskType) = taskDescriptor
}