package io.codestream.runtime.modules.json

import com.fasterxml.jackson.databind.ObjectMapper
import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter

class Parse(
        @Parameter(description="JSON string to parse")
        val json:String,
        @Parameter(description="name of output variable, result is a Map of Maps representing the JSON")
        override val outputVariable:String = "__output") : FunctionalTask {

    override fun getResult(ctx: RunContext): Any? {
        return ObjectMapper().readValue(json, java.util.Map::class.java)
    }

}