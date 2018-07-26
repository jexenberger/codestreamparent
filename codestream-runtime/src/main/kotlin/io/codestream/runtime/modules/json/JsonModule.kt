package io.codestream.runtime.modules.json

import com.fasterxml.jackson.databind.ObjectMapper
import io.codestream.api.KotlinModule

class JsonModule : KotlinModule("json", "Module for processing json") {

    init {
        add(Parse::class)
        add(ToJson::class)
    }

    companion object {
        val mapper = ObjectMapper()
    }

    override val scriptObject: Any?
        get() = JsonModuleFunctions()
}