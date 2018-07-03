package io.codestream.core.api.resources

interface WritableResourceRepository : ResourceRepository {

    fun save(resource:Resource)

}