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

package io.codestream.runtime.task

import io.codestream.api.*
import io.codestream.api.events.TaskErrorEvent
import io.codestream.api.metamodel.FunctionalTaskDef
import io.codestream.api.metamodel.TaskDef
import io.codestream.runtime.StreamContext
import io.codestream.runtime.tree.Node


class NonGroupTaskHandler(taskId: TaskId, taskDef: TaskDef) : BaseLeafTaskHandler(taskId, taskDef) {
    override fun run(ctx: StreamContext) {
        val task = try {
             ctx.get<Task>(taskId) ?: throw IllegalStateException("$taskId not resolved??!!")
        } catch (e:IllegalArgumentException) {
            throw ComponentFailedException(taskId.id, "Unable to create instance -> '${e}'")
        }
        when (task) {
            is FunctionalTask<*> -> {
                val result = task.evaluate(ctx.bindings)
                if (taskDef is FunctionalTaskDef && taskDef.assign != null) {
                    ctx.bindings[taskDef.assign!!] = result
                }
            }
            else -> {
                if (taskDef is FunctionalTaskDef) {
                    throw IllegalStateException("$taskId has a definition for a task that does not return a result")
                }
                (task as SimpleTask).run(ctx.bindings)
            }
        }
    }

    override fun visitWhenError(error: Exception, leaf: Node<StreamContext>, ctx: StreamContext): Exception {
        val taskError = TaskError.bubble(taskId, error, ctx.bindings)
        ctx.events.publish(TaskErrorEvent(taskId, taskError))
        return taskError
    }

    override fun toString(): String {
        return "SimpleTask -> $taskId)"
    }
}