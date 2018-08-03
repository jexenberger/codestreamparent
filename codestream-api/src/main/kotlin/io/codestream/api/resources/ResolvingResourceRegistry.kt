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

package io.codestream.api.resources

class ResolvingResourceRegistry(private var primaryResourceRepository: WritableResourceRepository) : WritableResourceRepository {

    private var registries: MutableCollection<ResourceRepository> = mutableSetOf(primaryResourceRepository)

    override fun save(resource: Resource) {
        primaryResourceRepository.save(resource)
    }

    override fun get(type: ResourceType, id: String): Resource? {
        for (registry in registries) {
            val lookup = registry[type, id]
            if (lookup != null) {
                return lookup
            }
        }
        return null
    }

    override fun find(type: ResourceType, attributes: Map<String, Any?>): Collection<Resource> {
        val found = mutableListOf<Resource>()
        for (registry in registries) {
            found.addAll(registry.find(type, attributes))
        }
        return found
    }

    override fun find(type: ResourceType): Collection<Resource> {
        val found = mutableListOf<Resource>()
        for (registry in registries) {
            found.addAll(registry.find(type))
        }
        return found
    }

    @Synchronized
    fun setPrimary(primaryResourceRepository: WritableResourceRepository) {
        this.primaryResourceRepository = primaryResourceRepository
    }


    operator fun plusAssign(repository: ResourceRepository) {
        synchronized(registries) {
            this.registries.add(repository)
        }
    }




}