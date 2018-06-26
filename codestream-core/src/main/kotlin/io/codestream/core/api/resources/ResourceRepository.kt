package io.codestream.core.api.resources

interface ResourceRepository {

    operator fun get(type:String, id: String): Resource?

    fun findByAttributes(vararg attributes: Pair<String, Any?>) = find(attributes.toMap())

    fun find(attributes: Map<String, Any?>): Collection<Resource>

    fun findByType(type: String): Collection<Resource>
}