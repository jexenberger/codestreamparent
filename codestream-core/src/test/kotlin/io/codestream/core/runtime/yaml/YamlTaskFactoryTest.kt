package io.codestream.core.runtime.yaml

import io.codestream.core.TestModule
import io.codestream.core.api.TaskType
import io.codestream.core.runtime.ModuleRegistry
import io.codestream.core.runtime.StreamContext
import io.codestream.core.runtime.TaskId
import org.junit.jupiter.api.Test
import java.io.File

class YamlTaskFactoryTest {

    private val file = File("src/test/resources/samplemodule/sample.yaml")
    val path = File("src/test/resources/samplemodule")
    val module = YamlModule(path)


    @Test
    internal fun testGet() {
        ModuleRegistry +=  module
        ModuleRegistry += TestModule()
        val type = TaskType.fromString("samplemodule::sample")
        val ctx = StreamContext()
        val compositeTask = YamlTaskFactory(module).get(TaskId(type), ctx)
        compositeTask.run(ctx.bindings)
    }
}