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

import io.codestream.api.annotations.Task
import org.junit.jupiter.api.Test
import kotlin.reflect.full.findAnnotation
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KotlinModuleTest {

    @Test
    internal fun testToTypeDescriptor() {
        val aModule = KotlinModule("test", "test")
        val descriptor = KotlinModule.typeToDescriptor(aModule, SampleSimpleTask::class)
        val task = SampleSimpleTask::class.findAnnotation<Task>()!!
        assertEquals(task.name, descriptor.name)
        assertEquals(task.description, descriptor.description)
        assertTrue { descriptor.parameters.containsKey("value") }
        assertTrue { descriptor.parameters.containsKey("anotherValue") }
        val property = descriptor.parameters["value"]!!
        assertEquals("value", property.name)
        assertEquals("description", property.description)
        val anotherProperty = descriptor.parameters["anotherValue"]!!
        assertEquals("anotherValue", anotherProperty.name)
        assertEquals("another description", anotherProperty.description)
    }

    @Test
    internal fun testName() {
        val aModule = KotlinModule("test", "test")
        val id = aModule.id
        assertEquals("test@1.0.0", id.toString())
    }

    @Test
    internal fun testAdd() {
        val aModule = KotlinModule("test", "test")
        aModule.add(SampleSimpleTask::class)
        assertNotNull(aModule[TaskType(aModule.id, "simpleTask")])

    }

    @Test
    internal fun testResolveVersion() {
        val version = KotlinModule.resolveVersion(KotlinModule::class)
    }
}