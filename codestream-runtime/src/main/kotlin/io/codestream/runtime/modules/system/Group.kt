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

import io.codestream.api.GroupTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Task
import io.codestream.api.Directive

@Task(name = "group", description = "Groups a set of related tasks together, allows the tasks to be executed conditionally or in parallel")
class Group : GroupTask {
    override fun before(ctx: RunContext) = Directive.continueExecution

    override fun after(ctx: RunContext) = Directive.done

    override fun onFinally(ctx: RunContext) {
    }

}