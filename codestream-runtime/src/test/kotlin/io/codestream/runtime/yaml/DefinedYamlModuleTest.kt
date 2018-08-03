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

import de.skuzzle.semantic.Version
import io.codestream.api.ModuleId
import io.codestream.api.TaskId
import io.codestream.api.TaskType
import io.codestream.runtime.StreamContext
import io.codestream.runtime.services.CodestreamScriptingService
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DefinedYamlModuleTest {

    val module = DefinedYamlModule(File("src/test/resources/samplemodule"), CodestreamScriptingService())

    @Test
    internal fun testCreate() {

        val descriptor = module[TaskType(ModuleId("samplemodule", module.version), "sample")]
        assertNotNull(descriptor)
        assertEquals(Version.create(1, 2, 3), module.version)
    }

    @Test
    internal fun testGetCompositeTask() {
        val type = TaskType(ModuleId("samplemodule", module.version), "sample")
        val task = module.getCompositeTask(TaskId(type), StreamContext())
        assertEquals(type, task.descriptor.type)
    }


    @Test
    internal fun testTaskDocumentation() {
        val taskDocumentation = module.taskDocumentation
        assertEquals(2, taskDocumentation.size)
        println(taskDocumentation)
    }
}