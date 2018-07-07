package io.codestream.sample

import io.codestream.core.api.RunContext
import io.codestream.core.api.SimpleTask
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.annotations.Task

@Task(name="greeter",description = "Greet a person")
class Greeter(
        @Parameter(description = "Name of the person you want to greet")
        val name:String
) : SimpleTask {
    override fun run(ctx: RunContext) {
        println("hello $name")
        ctx["greeted"] = name
    }
}