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

import io.codestream.api.ResourceException
import java.io.File

data class ResourceType(val category: String, val name: String) {


    val pathSnippet: File = File(category, name)
    val qualifiedName = "$category::$name"

    override fun toString(): String {
        return qualifiedName
    }

    companion object {
        fun fromString(str: String): ResourceType {
            val parts = str.split("::")
            return ResourceType(parts[0], parts[1])
        }
    }
}