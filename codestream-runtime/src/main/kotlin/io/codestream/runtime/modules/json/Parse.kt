package io.codestream.runtime.modules.json

import com.fasterxml.jackson.databind.ObjectMapper
import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task

@Task(name = "parse", description = "Parses a json string into a Map of Maps")
class Parse(
        @Parameter(description="JSON string to parse")
        val json:String) : FunctionalTask<Map<String, Any?>> {

    override fun evaluate(ctx: RunContext): Map<String, Any?>? {
        return ObjectMapper().readValue(json, java.util.Map::class.java) as Map<String, Any?>
    }

}