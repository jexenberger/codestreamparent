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

import io.codestream.util.rootCause
import io.codestream.util.stackDump

class TaskError(val taskId: TaskId, val exception: Exception, val runContext: RunContext) : Exception("$taskId -> '$exception'") {
    val rootCause: String = exception.rootCause.stackDump

    companion object {
        fun bubble(taskId: TaskId, exception: Exception, runContext: RunContext) : TaskError {
            return if (exception is TaskError) exception else TaskError(taskId, exception, runContext)
        }
    }
}