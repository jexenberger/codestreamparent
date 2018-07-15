package io.codestream.api.resources

data class Resource(val type: ResourceType, val id: String, val properties: Map<String, Any?>) : java.util.AbstractMap<String, Any?>() {
    override val entries: MutableSet<MutableMap.MutableEntry<String, Any?>>
        get() {
            val nMap = mutableMapOf<String, Any?>()
            nMap.putAll(properties)
            return nMap.entries
        }

    override fun put(key: String?, value: Any?): Any? {
        return super.put(key, value)
    }


    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other !is Resource) {
             return false
        }
        return this.id.equals(other.id) and this.type.equals(other.type)
    }
}