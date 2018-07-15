package io.codestream.runtime.modules.resources

import io.codestream.api.KotlinModule

class ResourcesModule : KotlinModule("resources", "Module for interacting with resources") {

    init {
        add(DefineResource::class)
    }

}