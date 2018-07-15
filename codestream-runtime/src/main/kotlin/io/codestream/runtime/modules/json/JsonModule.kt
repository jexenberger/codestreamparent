package io.codestream.runtime.modules.json

import io.codestream.api.KotlinModule

class JsonModule : KotlinModule("json", "Module for processing json") {

    init {
        add(Parse::class)
        add(ToJson::class)
    }

}