package io.codestream.core.runtime

import io.codestream.core.SampleSimpleTask
import io.codestream.core.api.BasicModule
import io.codestream.core.api.SimpleTask
import io.codestream.core.api.TaskType
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.typeToDescriptor
import io.codestream.core.runtime.metamodel.ParameterDef
import io.codestream.core.runtime.metamodel.TaskDef
import io.codestream.core.runtime.container.ParameterDependency
import io.codestream.di.api.DependencyTarget
import io.codestream.di.api.addInstance
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class ParameterDependencyTest {


    val aModule: BasicModule = BasicModule("test", "test")
    val descriptor = typeToDescriptor(aModule, SampleSimpleTask::class)
    val parameter = SampleSimpleTask::class.primaryConstructor!!.parameters.map { it.name to it }.toMap()
    val annotation = parameter["value"]?.findAnnotation<Parameter>() ?: fail("looks like SampleSimpleTaskChanged....")
    val ctx = StreamContext()
    val taskId = TaskId(TaskType(aModule.name, "test"))

    @Test
    internal fun testResolve() {

        ctx.bindings["hello"] = "world"
        ctx.bindings["anArray"] = arrayOf("1", "2")
        ctx.bindings["intArray"] = arrayOf(1, 2)
        ctx.bindings["aMap"] = mapOf("one" to "two", "two" to "three")
        ctx.bindings["collection"] = listOf("1", "2")

        val taskDef = TaskDef(taskId,
                mapOf(
                        "value" to ParameterDef("name", "\${hello}"),
                        "simple" to ParameterDef("name", "simple"),
                        "arrayString" to ParameterDef("name", "\${anArray}"),
                        "arrayInt" to ParameterDef("name", "\${intArray}"),
                        "map" to ParameterDef("name", "\${aMap}")
                ),
                { true }
        )
        val taskDefNoScript = TaskDef(taskId, mapOf("value" to ParameterDef("name", "hello")), { true })

        val idOne = TaskId(TaskType(aModule.name, "test"))
        addInstance(taskDef) withId TaskDefId(idOne) into ctx
        val idTwo = TaskId(TaskType(aModule.name, "testTwo"))
        addInstance(taskDefNoScript) withId TaskDefId(idTwo) into ctx

        parameter.forEach { (k, v) ->
            val result = ParameterDependency().resolve<Any>(annotation, DependencyTarget(idOne, SimpleTask::class, v), ctx)
        }

        /*
        val result =
        assertEquals("world", result)

        val resultTwo = ParameterDependency().resolve<String>(annotation, DependencyTarget(idTwo, SimpleTask::class, parameter), ctx)
        assertEquals("hello", resultTwo)*/
    }
}