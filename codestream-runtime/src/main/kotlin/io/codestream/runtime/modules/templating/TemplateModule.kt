package io.codestream.runtime.modules.templating

import io.codestream.api.KotlinModule

class TemplateModule : KotlinModule("template","Module for generating mustache template") {

    init {
        add(Generate::class)
    }

}