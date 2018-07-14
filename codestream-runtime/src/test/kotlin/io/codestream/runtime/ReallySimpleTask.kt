package io.codestream.runtime

import io.codestream.api.RunContext
import io.codestream.api.SimpleTask
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task

@Task("test", "A simple task which does stuff")
class ReallySimpleTask(
        @Parameter(description = "description") val value: String
) : SimpleTask {
    override fun run(ctx: RunContext) {
        println(value)
    }
}