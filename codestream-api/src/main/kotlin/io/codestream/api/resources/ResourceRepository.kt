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

interface ResourceRepository {

    operator fun get(type: ResourceType, id: String): Resource?

    fun findByAttributes(type: ResourceType, vararg attributes: Pair<String, Any?>) = find(type, attributes.toMap())

    fun find(type: ResourceType, attributes: Map<String, Any?>): Collection<Resource>

    fun find(type: ResourceType): Collection<Resource>
}