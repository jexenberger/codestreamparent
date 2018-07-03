package io.codestream.core.api.resources

import java.util.*

data class Resource(val type: ResourceType, val id: String, val properties: Map<String, Any?>) : java.util.AbstractMap<String, Any?>() {
    override val entries: MutableSet<MutableMap.MutableEntry<String, Any?>>
        get() {
            val nMap = mutableMapOf<String, Any?>()
            nMap.putAll(properties)
            return nMap.entries
        }

    override fun put(key: String?, value: Any?): Any? {
        if (!type.template.parameters.containsKey(key)) {
            throw IllegalArgumentException("$key is not a recognised property")
        }
        return super.put(key, value)
    }


    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other !is Resource) {
             return false
        }
        val naming = this.id.equals(other.id) and this.type.equals(other.type)
        val allPropsEqual = this.properties.map { (k, v) ->
            val otherVal = other[k]
            if (v is Array<*> && otherVal is Array<*>) Arrays.equals(v, otherVal) else Objects.equals(v, otherVal)
        }.fold(true) { a, b -> a and b}
        return naming && allPropsEqual
    }
}