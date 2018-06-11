package io.codestream.core.runtime

import io.codestream.core.SampleSimpleTask
import io.codestream.core.api.BasicModule
import io.codestream.core.api.SimpleTask
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.typeToDescriptor
import io.codestream.core.metamodel.PropertyDef
import io.codestream.core.metamodel.StreamDef
import io.codestream.core.metamodel.TaskDef
import io.codestream.di.api.DependencyTarget
import io.codestream.di.api.addInstance
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.test.assertEquals

class ParameterDependencyTest {

    @Test
    internal fun testResolve() {
        val aModule = BasicModule("test", "test")
        val descriptor = typeToDescriptor(aModule, SampleSimpleTask::class)
        val parameter = SampleSimpleTask::class.primaryConstructor!!.parameters[0]
        val annotation = parameter.findAnnotation<Parameter>() ?: fail("looks like SampleSimpleTaskChanged....")
        val ctx = StreamContext()
        val streamDef = StreamDef("test", "test", emptyList())
        val taskDef = TaskDef(streamDef, "test", { true }, Type.string, "test", mapOf("value" to PropertyDef("name", "\${hello}", descriptor.properties["value"]!!)))
        val taskDefNoScript = TaskDef(streamDef, "test", { true }, Type.string, "test", mapOf("value" to PropertyDef("name", "hello", descriptor.properties["value"]!!)))

        addInstance(taskDef) withId TaskDefId(TaskId("test")) into ctx
        addInstance(taskDefNoScript) withId TaskDefId(TaskId("testTwo")) into ctx

        ctx.bindings["hello"] = "world"

        val result = ParameterDependency().resolve<String>(annotation, DependencyTarget(TaskId("test"), SimpleTask::class, parameter), ctx)
        assertEquals("world", result)

        val resultTwo = ParameterDependency().resolve<String>(annotation, DependencyTarget(TaskId("testTwo"), SimpleTask::class, parameter), ctx)
        assertEquals("hello", resultTwo)
    }
}