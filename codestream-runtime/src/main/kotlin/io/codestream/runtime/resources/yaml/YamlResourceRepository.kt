/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.codestream.runtime.resources.yaml

import io.codestream.api.ResourceException
import io.codestream.api.resources.*
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
        return Resource(type, id, resourceData)
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