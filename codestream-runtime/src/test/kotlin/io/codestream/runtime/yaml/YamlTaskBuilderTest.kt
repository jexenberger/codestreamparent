package io.codestream.runtime.yaml

import io.codestream.api.ModuleId
import io.codestream.api.TaskId
import io.codestream.api.TaskType
import io.codestream.api.Type
import io.codestream.runtime.CompositeTask
import io.codestream.runtime.StreamContext
import io.codestream.runtime.services.CodestreamScriptingService
import org.junit.jupiter.api.Test
import java.io.File
import java.util.concurrent.Executors
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class YamlTaskBuilderTest {


    private val file = File("src/test/resources/samplemodule/sample.yaml")
    val path = File("src/test/resources/samplemodule")
    val module = DefinedYamlModule(path, CodestreamScriptingService())

    @Test
    internal fun testLoad() {
        val descriptor = YamlTaskBuilder(file.nameWithoutExtension, module, file.readText()).load()
        assertEquals(module, descriptor.module)
        assertEquals(descriptor.name, "sample")
        assertEquals(descriptor.description, "this is the uber cool task")
        assertEquals(5, descriptor.parameters.size)

        val parameter = descriptor["value1"] ?: fail("should have returned a parameter descriptor of 'value1'")
        assertEquals(Type.int, parameter.type)
        assertEquals("this is value 1", parameter.description)

        val parameter3 = descriptor["value3"] ?: fail("should have returned a parameter descriptor of 'value3'")
        assertEquals(Type.stringArray, parameter3.type)
        assertEquals(3, parameter3.allowedValues.size)
        listOf<String>("one", "two", "three").forEach { assertTrue { parameter3.allowedValues.contains(it) } }

        val parameter4 = descriptor["value4"] ?: fail("should have returned a parameter descriptor of 'value4'")
        assertEquals(Type.stringArray, parameter4.type)
        assertEquals(3, parameter4.allowedValues.size)
        listOf<String>("one", "two", "three").forEach { assertTrue { parameter4.allowedValues.contains(it) } }
    }

    @Test
    internal fun testDefineTaskTree() {
        val descriptor = YamlTaskBuilder(file.nameWithoutExtension, module, file.readText(), Executors.newSingleThreadExecutor()).load()
        val task = CompositeTask(TaskId(TaskType(ModuleId.fromString("sample.yaml"), "my-cool-task")), descriptor, StreamContext())
        YamlTaskBuilder(file.nameWithoutExtension, module, file.readText()).defineTaskTree(task)
        assertTrue { task.children.isNotEmpty() }
        val calledList = mutableListOf<String>()
        task.dump()
        task.traverse { node, parent->
            calledList += node.id
        }
        assertTrue { calledList.size > 0 }
    }
}