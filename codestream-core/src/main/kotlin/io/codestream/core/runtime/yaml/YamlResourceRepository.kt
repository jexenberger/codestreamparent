package io.codestream.core.runtime.yaml

import io.codestream.core.api.resources.Resource
import io.codestream.core.api.resources.ResourceRepository
import io.codestream.util.system
import java.io.File

class YamlResourceRepository(val path:File = File("${system.homeDir}/.cs/resource-registry")) : ResourceRepository {
    override fun get(type: String, id: String): Resource? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun find(attributes: Map<String, Any?>): Collection<Resource> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByType(type: String): Collection<Resource> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}