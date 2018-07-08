package io.codestream.core.runtime.modules.system

import io.codestream.core.api.SimpleTask
import io.codestream.core.api.RunContext
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.annotations.Task
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