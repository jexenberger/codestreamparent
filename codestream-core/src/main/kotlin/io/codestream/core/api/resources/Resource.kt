package io.codestream.core.api.resources

class Resource(val type:String, val id:String) : AbstractMap<String, Any?>() {
    override val entries: Set<Map.Entry<String, Any?>>
        get() = emptyMap<String, Any>().entries
}