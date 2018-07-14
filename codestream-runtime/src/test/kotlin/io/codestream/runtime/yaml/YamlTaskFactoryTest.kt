package io.codestream.runtime.yaml

import io.codestream.api.TaskId
import io.codestream.api.TaskType
import io.codestream.runtime.ModuleRegistry
import io.codestream.runtime.StreamContext
import io.codestream.runtime.TestModule
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertTrue

class YamlTaskFactoryTest {

    private val file = File("src/test/resources/samplemodule/sample.yaml")
    val path = File("src/test/resources/samplemodule")
    val module = DefinedYamlModule(path)


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