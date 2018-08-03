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

import io.codestream.api.ModuleId
import io.codestream.api.TaskType
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SingleFileModuleTest {

    private val standaloneFile = File("src/test/resources/standalone/standalonetask.yaml")
    private  val module = SingleFileModule(standaloneFile)

    @Test
    internal fun testTaskDescription() {
        val result = module.get(TaskType(ModuleId.fromString(standaloneFile.parentFile.absolutePath), "standalone"))
        assertNotNull(result)
        println(result.type)
    }

    @Test
    internal fun testName() {

        assertEquals(standaloneFile.parentFile.absolutePath, module.name)
    }
    @Test
    internal fun testDescription() {
        assertEquals("This is a simple standalone task", module.description)
    }
}