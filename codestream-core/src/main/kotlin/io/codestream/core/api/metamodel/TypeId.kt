package io.codestream.core.api.metamodel

import io.codestream.di.api.ComponentId

data class TypeId(val module:String, val name:String, val version:String = "1.0") : ComponentId {
    override val stringId: String
        get() = "$module@$version::$name"

}