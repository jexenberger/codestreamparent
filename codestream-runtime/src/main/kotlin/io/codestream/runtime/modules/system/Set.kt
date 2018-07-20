package io.codestream.runtime.modules.system

import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.SimpleTask
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task

@Task(name = "set", description = "Displays a value to the system console")
class Set(
        @Parameter(description = "value to set for the against the name in the outputVariable", required = false)
        val value: Any?,
        @Parameter(description = "value to set for the against the name in the outputVariable")
        val outputVariable: String
) : SimpleTask{

    override fun run(ctx: RunContext) {
        ctx[outputVariable] = value
    }
}