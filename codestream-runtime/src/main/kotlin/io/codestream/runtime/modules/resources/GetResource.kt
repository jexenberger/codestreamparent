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

package io.codestream.runtime.modules.resources

import io.codestream.api.ComponentFailedException
import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.SimpleTask
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task
import io.codestream.api.resources.*


@Task(name = "get", description = "Defines a resource against a specific type with an id, if the type and id combination exist the existing resource is overwritten")
class GetResource(
        @Parameter(description = "id to the resource to define")
        val id: String,
        @Parameter(description = "Name of resource type to define")
        val type: String
) : FunctionalTask<Resource> {

    override fun evaluate(ctx: RunContext): Resource? {
        val registry = ctx["_resources"] as WritableResourceRepository? ?: throw ComponentFailedException("get-resource","no resource registry defined")
        val resourceType = ResourceType.fromString(type)
        return registry.get(resourceType, id)
    }
}