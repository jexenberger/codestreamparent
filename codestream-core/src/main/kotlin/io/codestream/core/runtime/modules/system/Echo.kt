package io.codestream.core.runtime.modules.system

import io.codestream.core.api.SimpleTask
import io.codestream.core.api.RunContext
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.annotations.Task

@Task(name = "echo", description = "Displays a value to the system console")
class Echo(
        @Parameter(description = "Value to echo to the Console") val value: String?

) : SimpleTask {
    override fun run(ctx: RunContext) {
        println("$value")
    }
}