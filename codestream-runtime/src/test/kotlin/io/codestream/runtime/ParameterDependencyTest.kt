package io.codestream.runtime

import io.codestream.api.KotlinModule
import io.codestream.api.SimpleTask
import io.codestream.api.TaskId
import io.codestream.api.TaskType
import io.codestream.api.annotations.Parameter
import io.codestream.api.metamodel.ParameterDef
import io.codestream.api.metamodel.TaskDef
import io.codestream.api.services.ScriptService
import io.codestream.di.api.DependencyTarget
import io.codestream.di.api.TypeId
import io.codestream.di.api.addInstance
import io.codestream.runtime.container.ParameterDependency
import io.codestream.runtime.services.CodestreamScriptingService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.test.assertNotNull

class ParameterDependencyTest {


    val aModule: KotlinModule = KotlinModule("test", "test")
    val descriptor = KotlinModule.typeToDescriptor(aModule, SampleSimpleTask::class)
    val parameter = SampleSimpleTask::class.primaryConstructor!!.parameters.map { it.name to it }.toMap()
    val annotation = parameter["value"]?.findAnnotation<Parameter>() ?: fail("looks like SampleSimpleTaskChanged....")
    val ctx = StreamContext()
    val taskId = TaskId(TaskType(aModule.id, "test"))

    @Test
    internal fun testResolve() {

        addInstance(CodestreamScriptingService()) withId TypeId(ScriptService::class) into ctx

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

        val idOne = TaskId(TaskType(aModule.id, "test"))
        addInstance(taskDef) withId TaskDefId(idOne) into ctx
        val idTwo = TaskId(TaskType(aModule.id, "testTwo"))
        addInstance(taskDefNoScript) withId TaskDefId(idTwo) into ctx

        parameter.forEach { (k, v) ->
            val result = ParameterDependency().resolve<Any>(annotation, DependencyTarget(idOne, SimpleTask::class, v), ctx)
            assertNotNull(result)
        }
    }
}