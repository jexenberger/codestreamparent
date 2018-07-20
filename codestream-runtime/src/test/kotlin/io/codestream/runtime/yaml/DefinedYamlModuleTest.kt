package io.codestream.runtime.yaml

import de.skuzzle.semantic.Version
import io.codestream.api.ModuleId
import io.codestream.api.TaskId
import io.codestream.api.TaskType
import io.codestream.runtime.StreamContext
import io.codestream.runtime.services.CodestreamScriptingService
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DefinedYamlModuleTest {

    val module = DefinedYamlModule(File("src/test/resources/samplemodule"), CodestreamScriptingService())

    @Test
    internal fun testCreate() {

        val descriptor = module[TaskType(ModuleId("samplemodule", module.version), "sample")]
        assertNotNull(descriptor)
        assertEquals(Version.create(1, 2, 3), module.version)
    }

    @Test
    internal fun testGetCompositeTask() {
        val type = TaskType(ModuleId("samplemodule", module.version), "sample")
        val task = module.getCompositeTask(TaskId(type), StreamContext())
        assertEquals(type, task.descriptor.type)
    }

    @Test
    internal fun testScriptObjectDocumentation() {
        val scriptDocumentation = module.scriptObjectDocumentation
        assertEquals(2, scriptDocumentation.size)
        scriptDocumentation.forEach {
            println(it)
        }
    }

    @Test
    internal fun testTaskDocumentation() {
        val taskDocumentation = module.taskDocumentation
        assertEquals(2, taskDocumentation.size)
        println(taskDocumentation)
    }
}