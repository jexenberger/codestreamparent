package io.codestream.core.runtime.yaml

import de.skuzzle.semantic.Version
import io.codestream.core.api.ModuleId
import io.codestream.core.api.TaskType
import io.codestream.core.api.TaskId
import io.codestream.core.runtime.StreamContext
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DefinedYamlModuleTest {


    @Test
    internal fun testCreate() {
        val module = DefinedYamlModule(File("src/test/resources/samplemodule"))
        val descriptor = module[TaskType(ModuleId("samplemodule", module.version), "sample")]
        assertNotNull(descriptor)
        assertEquals(Version.create(1, 2, 3), module.version)
    }

    @Test
    internal fun testGetCompositeTask() {
        val module = DefinedYamlModule(File("src/test/resources/samplemodule"))
        val type = TaskType(ModuleId("samplemodule", module.version), "sample")
        val task = module.getCompositeTask(TaskId(type), StreamContext())
        assertEquals(type, task.descriptor.type)
    }

    @Test
    internal fun testScriptObjectDocumentation() {
        val module = DefinedYamlModule(File("src/test/resources/samplemodule"))
        val scriptDocumentation = module.scriptObjectDocumentation
        assertEquals(2, scriptDocumentation.size)
        scriptDocumentation.forEach {
            println(it)
        }
    }

    @Test
    internal fun testTaskDocumentation() {
        val module = DefinedYamlModule(File("src/test/resources/samplemodule"))
        val taskDocumentation = module.taskDocumentation
        assertEquals(1, taskDocumentation.size)
        println(taskDocumentation)
    }
}