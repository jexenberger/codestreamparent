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

package io.codestream.runtime

import io.codestream.api.Directive
import io.codestream.api.GroupTask
import io.codestream.api.RunContext
import io.codestream.api.TaskError
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.api.annotations.TaskContext

@Task("group", "A group task which does stuff")
class SimpleGroupTask(
        @Parameter(description = "description", default = "test") val value: String,
        @TaskContext val context: SimpleGroupTaskContext
) : GroupTask {

    override fun before(ctx: RunContext): Directive {
        context.before = true
        return Directive.continueExecution
    }

    override fun after(ctx: RunContext): Directive {
        context.after = true
        return Directive.done
    }

    override fun onError(error: TaskError, ctx: RunContext) {
        context.error = true
    }

    override fun onFinally(ctx: RunContext) {
        context.onFinally = true
    }


}