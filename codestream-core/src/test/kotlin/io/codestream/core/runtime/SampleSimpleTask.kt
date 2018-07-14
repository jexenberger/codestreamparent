package io.codestream.runtime

import io.codestream.core.api.SimpleTask
import io.codestream.core.api.RunContext
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.annotations.Task

@Task("simpleTask","A simple task which does stuff")
class SampleSimpleTask(
        @Parameter(description = "description") val value:String,
        @Parameter(description = "simple") val simple:String,
        @Parameter(description = "arrayString") val arrayString:Array<String>,
        @Parameter(description = "arrayInt") val arrayInt:Array<Int>,
        @Parameter(description = "map") val map:Map<String, Any?>) : SimpleTask {

    @Parameter(description = "another description")
    var anotherValue: String = ""


    var run = false

    override fun run(ctx: RunContext) {
        println(value)
        run = true
    }
}