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

data class Resource(val type: ResourceType, val id: String, val properties: Map<String, Any?>) : java.util.AbstractMap<String, Any?>() {
    override val entries: MutableSet<MutableMap.MutableEntry<String, Any?>>
        get() {
            val nMap = mutableMapOf<String, Any?>()
            nMap.putAll(properties)
            return nMap.entries
        }

    override fun put(key: String?, value: Any?): Any? {
        return super.put(key, value)
    }


    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other !is Resource) {
             return false
        }
        return this.id.equals(other.id) and this.type.equals(other.type)
    }
}