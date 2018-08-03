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

import io.codestream.di.event.EventHandler
import io.codestream.doc.ModuleDoc
import java.io.File

abstract class Codestream {

    abstract val modules:Set<ModuleId>

    abstract fun runTask(module: ModuleId, task: String, parameters: Map<String, Any?>, callback: ParameterCallback) : Pair<Any, Map<String, Any?>>
    abstract fun runTask(module: ModuleId, task: String, parameters: Map<String, Any?>) : Pair<Any, Map<String, Any?>>
    abstract fun runTask(file: File, parameters: Map<String, Any?>, callback: ParameterCallback): Pair<Any, Map<String, Any?>>

    abstract fun moduleDoc(name:ModuleId) : ModuleDoc?
    abstract fun taskDoc(name:TaskType) : TaskType?

    abstract fun shutdown()

    val eventHandlers = mutableSetOf<EventHandler<*>>()


    fun modulesByName(name:String) : Set<ModuleId> = modules.filter { it.name.equals(name) }.toSet()

    operator fun plusAssign(handler:EventHandler<*>) {
        eventHandlers += handler
    }



}