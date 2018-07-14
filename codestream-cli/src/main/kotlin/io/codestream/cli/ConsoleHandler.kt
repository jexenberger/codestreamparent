package io.codestream.cli

import io.codestream.core.api.events.AfterTaskEvent
import io.codestream.core.api.events.BeforeTaskEvent
import io.codestream.core.api.events.TaskErrorEvent
import io.codestream.core.api.events.TaskEvent
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
                Console.display(decorate(event.taskId.taskType.taskName, Console.REVERSED, Console.ANSI_RED))
                        .space()
                        .display(decorate(event.error.message, Console.BOLD))
                        .space()
                        .newLine()
                if (enableDebug) {
                    Console.display(event.error.rootCause).newLine()
                }
            }
        }

    }
}