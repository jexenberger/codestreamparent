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

package io.codestream.runtime.modules.json

import com.fasterxml.jackson.databind.ObjectMapper
import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task

@Task(name = "parse", description = "Parses a json string into a Map of Maps")
class Parse(
        @Parameter(description="JSON string to parse")
        val json:String) : FunctionalTask<Map<String, Any?>> {

    override fun evaluate(ctx: RunContext): Map<String, Any?>? {
        return JsonModule.mapper.readValue(json, java.util.Map::class.java) as Map<String, Any?>
    }

}