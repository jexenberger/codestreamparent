package io.codestream.core.runtime.yaml

import io.codestream.core.runtime.TestModule
import io.codestream.core.api.TaskType
import io.codestream.core.runtime.ModuleRegistry
import io.codestream.core.runtime.StreamContext
import io.codestream.core.api.TaskId
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