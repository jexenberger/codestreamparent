package io.codestream.core.runtime

import io.codestream.core.api.SimpleTask
import io.codestream.core.api.RunContext
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.annotations.Task

@Task("test", "A simple task which does stuff")
class ReallySimpleTask(
        @Parameter(description = "description") val value: String
) : SimpleTask {
    override fun run(ctx: RunContext) {
        println(value)
    }
}