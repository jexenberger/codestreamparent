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

package io.codestream.api

import io.codestream.api.annotations.Parameter
import io.codestream.api.annotations.Task

@Task("simpleTask","A simple task which does stuff")
class SampleSimpleTask(
        @Parameter(description = "description") val value:String,
        @Parameter(description = "simple") val simple:String,
        @Parameter(description = "arrayString") val arrayString:Array<String>,
        @Parameter(description = "arrayInt") val arrayInt:Array<Int>,
        @Parameter(description = "map") val map:Map<String, Any?>) : SimpleTask {

    @Parameter(description = "another description")
    var anotherValue: String = ""


    var run = false

    override fun run(ctx: RunContext) {
        println(value)
        run = true
    }
}