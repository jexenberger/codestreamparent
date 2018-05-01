package io.codestream.di.api

import io.codestream.di.api.Context
import io.codestream.util.OS
import javax.script.SimpleBindings

class DependencyInjectionBindings(ctx: Context, m: MutableMap<String, Any>? = mutableMapOf()) : SimpleBindings(m) {

    init {
       put("_ctx", ctx)
       put("_os", OS.os())
    }

}