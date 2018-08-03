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

package io.codestream.runtime.yaml

import io.codestream.api.TaskId
import io.codestream.api.TaskType
import io.codestream.runtime.ModuleRegistry
import io.codestream.runtime.StreamContext
import io.codestream.runtime.TestModule
import io.codestream.runtime.services.CodestreamScriptingService
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertTrue

class YamlTaskFactoryTest {

    private val file = File("src/test/resources/samplemodule/sample.yaml")
    val path = File("src/test/resources/samplemodule")
    val module = DefinedYamlModule(path, CodestreamScriptingService())


    @Test
    internal fun testGet() {
        ModuleRegistry +=  module
        ModuleRegistry += TestModule()
        val type = TaskType.fromString("samplemodule::sample")
        val ctx = StreamContext()
        val compositeTask = YamlTaskFactory(module).get(TaskId(type), ctx)
        assertTrue { compositeTask.children.isNotEmpty() }
    }
}