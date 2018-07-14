package io.codestream.api.resources

interface ResourceRepository {

    operator fun get(type: ResourceType, id: String): Resource?

    fun findByAttributes(type: ResourceType, vararg attributes: Pair<String, Any?>) = find(type, attributes.toMap())

    fun find(type: ResourceType, attributes: Map<String, Any?>): Collection<Resource>

    fun find(type: ResourceType): Collection<Resource>
}