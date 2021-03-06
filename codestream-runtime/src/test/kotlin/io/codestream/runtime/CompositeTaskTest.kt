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

package io.codestream.runtime

import io.codestream.api.ModuleId
import io.codestream.api.TaskId
import io.codestream.api.TaskType
import io.codestream.api.metamodel.ParameterDef
import io.codestream.api.metamodel.TaskDef
import io.codestream.runtime.services.CodestreamScriptingService
import io.codestream.runtime.task.TaskDefContext
import io.codestream.runtime.yaml.DefinedYamlModule
import io.codestream.runtime.yaml.YamlTaskFactory
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class CompositeTaskTest {

    @Test
    internal fun testCompositeTask() {
        ModuleRegistry += TestModule()
        val scriptingService = CodestreamScriptingService()
        val module = DefinedYamlModule(File("src/test/resources/samplemodule"), scriptingService)
        ModuleRegistry += module
        val type = TaskType(ModuleId.fromString("samplemodule::1.2.3"), "sample")
        val context = StreamContext(scriptingService = scriptingService)
        val task = YamlTaskFactory(module).get(TaskId(type), context)
        val def = TaskDef(TaskId(type), mapOf(
                "value1" to ParameterDef("value1", 1),
                "value3" to ParameterDef("value3", "one, two, three"),
                "value4" to ParameterDef("value4", listOf("one", "two")),
                "value5" to ParameterDef("value5", mapOf("1" to "hello"))
        ))
        TaskDefContext.defn = def
        context.bindings["value1"] = 1
        context.bindings["value2"] = "Hello"
        context.bindings["value4"] = "Hello"
        task.evaluate(context.bindings)
    }

    @Test
    internal fun testFunctionalCompositeTask() {
        ModuleRegistry += TestModule()
        val scriptingService = CodestreamScriptingService()
        val module = DefinedYamlModule(File("src/test/resources/samplemodule"), scriptingService)
        ModuleRegistry += module
        val type = TaskType(ModuleId.fromString("samplemodule::1.2.3"), "samplefunctional")
        val context = StreamContext(scriptingService = scriptingService)
        val task = YamlTaskFactory(module).get(TaskId(type), context)
        val def = TaskDef(TaskId(type), mapOf(
                "value" to ParameterDef("value", "hello")
        ))
        TaskDefContext.defn = def
        context.bindings["value"] = "hello"
        val result = task.evaluate(context.bindings)
        assertEquals("HELLO", result)
    }
}