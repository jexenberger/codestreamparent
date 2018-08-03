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

package io.codestream.runtime.modules.system

import io.codestream.api.ComponentFailedException
import io.codestream.api.RunContext
import io.codestream.api.SimpleTask
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.runtime.task.TaskDefContext

@Task(name = "fail", description = "Fails the current task execution with a message")
class Fail(
        @Parameter(description = "message to display")
        val message: String
) : SimpleTask {

    override fun run(ctx: RunContext) {
        throw ComponentFailedException(TaskDefContext.defn.id.stringId, message)
    }
}