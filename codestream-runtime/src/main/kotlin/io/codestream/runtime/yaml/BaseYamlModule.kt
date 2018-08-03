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

import io.codestream.api.CodestreamModule
import io.codestream.api.TaskDoesNotExistException
import io.codestream.api.TaskId
import io.codestream.di.api.addInstance
import io.codestream.runtime.CompositeTask
import io.codestream.runtime.StreamContext
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

interface BaseYamlModule : CodestreamModule {


    val modulePath: String




    fun createScriptObjects(): Map<String, Any>

    fun getCompositeTask(id: TaskId, ctx: StreamContext): CompositeTask {
        val withThis = createScriptObjects()
        val taskDescriptor = getByName(id.taskType.name)
        taskDescriptor ?: throw TaskDoesNotExistException(id.taskType)
        val name = id.taskType.name
        val file = File(resolveTaskPath(name))
        val newContext = StreamContext(originatingContextId = ctx.originatingContextId)
        newContext.bindings["__modulePath"] = modulePath
        withThis.forEach { (k, v) ->
            newContext.bindings[k] = v
        }
        val task = CompositeTask(id, taskDescriptor, newContext)
        YamlTaskBuilder(name, this, file.readText()).defineTaskTree(task)
        return task
    }

    fun resolveTaskPath(name: String) : String

}