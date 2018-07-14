package io.codestream.api.resources

interface WritableResourceRepository : ResourceRepository {

    fun save(resource: Resource)

}