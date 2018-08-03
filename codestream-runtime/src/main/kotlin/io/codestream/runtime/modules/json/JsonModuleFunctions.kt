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

import io.codestream.api.annotations.ModuleFunction
import io.codestream.api.annotations.ModuleFunctionParameter

class JsonModuleFunctions {

    @ModuleFunction("Takes an array type and writes it in JSON format, returns the value in a String")
    fun arrayToJson(
            @ModuleFunctionParameter("array", "array of values ")
            array:Array<*>
    ) : String {
        return JsonModule.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(array)
    }

    @ModuleFunction("Takes a collection which is Iterable in JSON format, returns the value in a String")
    fun collectionToJson(
            @ModuleFunctionParameter("collection", "collection of values")
            collection:Iterable<*>
    ) : String {
        return JsonModule.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection.iterator())
    }

}