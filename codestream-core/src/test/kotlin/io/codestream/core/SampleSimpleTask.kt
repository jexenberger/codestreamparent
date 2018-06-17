package io.codestream.core

import io.codestream.core.api.SimpleTask
import io.codestream.core.api.TaskContext
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.annotations.Task
import io.codestream.core.metamodel.TaskDef

@Task("simpleTask","A simple task which does stuff")
class SampleSimpleTask(@Parameter(description = "description") val value:String) : SimpleTask {

    @Parameter(description = "another description")
    var anotherValue: String = ""

    var run = false

    override fun run(def: TaskDef, ctx: TaskContext) {
        println(value)
        run = true
    }
}