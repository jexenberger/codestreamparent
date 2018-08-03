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

import io.codestream.api.SimpleTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.util.io.console.Console
import io.codestream.util.io.console.info
import io.codestream.util.io.console.warn

@Task(name = "echo", description = "Displays a value to the system console")
class Echo(
        @Parameter(description = "Value to echo to the Console") val value: Any?,
        @Parameter(description = "Colour the output, valid options are 'red', 'green' or 'default'", default = "default") val type: Echo.Display

) : SimpleTask {

    enum class Display(val handler: (Any?) -> Unit) {
        red({
            Console.display(warn(it)).newLine()
        }),
        green({
            Console.display(info(it)).newLine()
        }),
        default({
            Console.display(it).newLine()
        })

    }

    override fun run(ctx: RunContext) {
        type.handler(value)
    }
}