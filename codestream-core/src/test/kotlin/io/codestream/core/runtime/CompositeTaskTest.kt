package io.codestream.core.runtime

import io.codestream.core.TestModule
import io.codestream.core.api.TaskType
import io.codestream.core.runtime.metamodel.ParameterDef
import io.codestream.core.runtime.metamodel.TaskDef
import io.codestream.core.runtime.task.TaskDefContext
import io.codestream.core.runtime.yaml.YamlModule
import io.codestream.core.runtime.yaml.YamlTaskFactory
import org.junit.jupiter.api.Test
import java.io.File

class CompositeTaskTest {

    @Test
    internal fun testCompositeTask() {
        ModuleRegistry += TestModule()
        val module = YamlModule(File("src/test/resources/samplemodule"))
        ModuleRegistry += module
        val type = TaskType("samplemodule", "sample")
        val context = StreamContext()
        val task = YamlTaskFactory(module).get(TaskId(type), context)
        val def = TaskDef(TaskId(type), mapOf(
                "value1" to ParameterDef("value1", 1),
                "value3" to ParameterDef("value3", "one, two, three"),
                "value4" to ParameterDef("value4", listOf("one", "two")),
                "value5" to ParameterDef("value5", mapOf("1" to "hello"))
        ))
        TaskDefContext.defn = def
        context.bindings["value1"] = 1
        context.bindings["value2"] = "Hello"
        context.bindings["value4"] = "Hello"
        task.run(context.bindings)
    }
}