package io.codestream.core.runtime.resources.yaml

import io.codestream.core.api.ResourceException
import io.codestream.core.api.resources.*
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileReader

class YamlResourceRepository(val databaseName: String, val basePath: File) : WritableResourceRepository {

    private val fullPath: File

    init {
        if (!basePath.exists()) {
            basePath.mkdirs()
        }
        if (!basePath.isDirectory) {
            throw ResourceException(databaseName, "'${basePath.absolutePath}' does not exist or is not a directory")
        }
        fullPath = File(basePath, databaseName)
        if (fullPath.exists() && !fullPath.isFile) {
            throw ResourceException(databaseName, "'${basePath.absolutePath}' already exists, unable to create resource database")
        }


    }

    override fun get(type: ResourceType, id: String): Resource? {
        val typePath = File(fullPath, type.pathSnippet.toString())
        if (!typePath.exists()) {
            return null
        }
        val idPath = File(typePath, id)
        if (!idPath.exists()) {
            return null
        }
        if (idPath.isDirectory) {
            return null
        }
        return loadResource(idPath, type, id)
    }

    private fun loadResource(idPath: File, type: ResourceType, id: String): Resource {
        val resourceData = Yaml().load(FileReader(idPath)) as Map<String, Any?>
        val properties = type.template.parameters.entries.map { (k, v) ->
            k to resourceData[k]?.let { v.type.convert<Any?>(it) }
        }.toMap()
        return Resource(type, id, properties)
    }

    override fun find(type: ResourceType): Collection<Resource> {
        val typePath = File(fullPath, type.pathSnippet.toString())
        if (!typePath.exists()) {
            return emptyList()
        }
        val files = typePath.listFiles { file: File -> file.isFile() }.sortedBy { it.name }
        return files.map { loadResource(it, type, it.name) }
    }

    override fun find(type: ResourceType,  attributes: Map<String, Any?>): Collection<Resource> {
        return find(type).filter {
            it.properties.map { (k, v) ->
                attributes[k]?.equals(v) ?: true
            }.fold(true) { a, b -> a and b}
        }
    }

    override fun save(resource: Resource) {
        val typePath = File(fullPath, resource.type.pathSnippet.toString())
        if (!typePath.exists()) {
            typePath.mkdirs()
        }
        val idPath = File(typePath, resource.id)
        idPath.writeText(Yaml().dump(resource.properties))
    }

}