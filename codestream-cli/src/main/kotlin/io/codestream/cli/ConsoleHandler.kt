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

package io.codestream.cli

import io.codestream.api.events.AfterTaskEvent
import io.codestream.api.events.BeforeTaskEvent
import io.codestream.api.events.TaskErrorEvent
import io.codestream.api.events.TaskEvent
import io.codestream.di.event.EventHandler
import io.codestream.util.io.console.Console
import io.codestream.util.io.console.decorate

class ConsoleHandler(val enableDebug: Boolean) : EventHandler<TaskEvent> {
    override fun onEvent(event: TaskEvent) {
        when (event) {
            is BeforeTaskEvent -> {
                Console.display(decorate(event.taskId.taskType.taskName, Console.REVERSED))
                        .newLine()
            }
            is AfterTaskEvent -> {
                val display = "${event.taskId.taskType.taskName} (${event.timeTaken}ms)"
                Console.display(decorate(display, Console.ANSI_GREEN,Console.REVERSED))
                        .newLine()

            }
            is TaskErrorEvent -> {
                Console.display(decorate(event.taskId, Console.REVERSED, Console.ANSI_RED))
                        .space()
                        .display(decorate(event.error.message, Console.BOLD))
                        .space()
                        .display(decorate(event.taskId, Console.BOLD))
                        .space()
                        .newLine()
                if (enableDebug) {
                    Console.display(event.error.rootCause).newLine()
                }
            }
        }

    }
}