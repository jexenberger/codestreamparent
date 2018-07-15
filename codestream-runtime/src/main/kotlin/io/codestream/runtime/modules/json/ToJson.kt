package io.codestream.runtime.modules.json

import com.fasterxml.jackson.databind.ObjectMapper
import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task

@Task(name = "to-json", description = "Turns a Map of Maps into a JSON string")
class ToJson(@Parameter(description = "set of parameters to dump")
             val input: Map<String, Any?>,
             @Parameter(description = "name of output variable to store the resulting JSON string")
             override val outputVariable: String = "__output") : FunctionalTask {
    override fun getResult(ctx: RunContext): Any? {
        return ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(input)
    }
}