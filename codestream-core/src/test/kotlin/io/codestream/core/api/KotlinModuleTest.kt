package io.codestream.core.api

import io.codestream.core.runtime.SampleSimpleTask
import io.codestream.core.api.annotations.Task
import org.junit.jupiter.api.Test
import kotlin.reflect.full.findAnnotation
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KotlinModuleTest {

    @Test
    internal fun testToTypeDescriptor() {
        val aModule = KotlinModule("test", "test")
        val descriptor = typeToDescriptor(aModule, SampleSimpleTask::class)
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