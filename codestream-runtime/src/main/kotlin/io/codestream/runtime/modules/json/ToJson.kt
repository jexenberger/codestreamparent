package io.codestream.runtime.modules.json

import com.fasterxml.jackson.databind.ObjectMapper
import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task

@Task(name = "to-json", description = "Turns a Map of Maps into a JSON string")
class ToJson(@Parameter(description = "set of parameters to dump")
             val input: Map<String, Any?>) : FunctionalTask<String> {
    override fun evaluate(ctx: RunContext): String? {
        return ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(input)
    }
}