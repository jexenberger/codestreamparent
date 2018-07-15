package io.codestream.runtime.modules.http

import io.codestream.api.KotlinModule
import io.codestream.api.defaultVersion

class HttpModule : KotlinModule("http", "module to perform HTTP operations", defaultVersion) {

    init {
        add(Get::class)
        add(Put::class)
        add(Post::class)
    }

}