package io.codestream.core.runtime

import io.codestream.core.SampleSimpleTask
import io.codestream.core.api.BasicModule
import io.codestream.core.api.SimpleTask
import io.codestream.core.api.TaskType
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.typeToDescriptor
import io.codestream.core.metamodel.ParameterDef
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
        val taskId = TaskId(TaskType(aModule.id, "test"))
        val taskDef = TaskDef(taskId, mapOf("value" to ParameterDef("name", "\${hello}")), { true })
        val taskDefNoScript = TaskDef(taskId,  mapOf("value" to ParameterDef("name", "hello")), { true })

        val idOne = TaskId(TaskType(aModule.id, "test"))
        addInstance(taskDef) withId TaskDefId(idOne) into ctx
        val idTwo = TaskId(TaskType(aModule.id, "testTwo"))
        addInstance(taskDefNoScript) withId TaskDefId(idTwo) into ctx

        ctx.bindings["hello"] = "world"

        val result = ParameterDependency().resolve<String>(annotation, DependencyTarget(idOne, SimpleTask::class, parameter), ctx)
        assertEquals("world", result)

        val resultTwo = ParameterDependency().resolve<String>(annotation, DependencyTarget(idTwo, SimpleTask::class, parameter), ctx)
        assertEquals("hello", resultTwo)
    }
}