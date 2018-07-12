package io.codestream.core.runtime.modules.system

import io.codestream.core.api.FunctionalTask
import io.codestream.core.api.RunContext
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.annotations.Task

@Task(name = "set", description = "Displays a value to the system console")
class Set(
        @Parameter(description = "name of the variable to set in the current context", default = "__outputVar")
        override val outputVariable: String,
        @Parameter(description = "value to set for the against the name in the outputVariable", required = false)
        val value: Any?
) : FunctionalTask{

    override fun getResult(ctx: RunContext): Any? {
        return value
    }
}