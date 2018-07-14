package io.codestream.api

import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task

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