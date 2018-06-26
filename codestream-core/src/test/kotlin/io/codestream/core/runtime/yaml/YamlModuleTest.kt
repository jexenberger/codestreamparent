package io.codestream.core.runtime.yaml

import de.skuzzle.semantic.Version
import io.codestream.core.api.TaskType
import io.codestream.core.runtime.TaskId
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class YamlModuleTest {


    @Test
    internal fun testCreate() {
        val module = YamlModule(File("src/test/resources/samplemodule"))
        val descriptor = module[TaskType("samplemodule","sample")]
        assertNotNull(descriptor)
        assertEquals(Version.create(1, 2, 3), module.version)
    }

    @Test
    internal fun testGetCompositeTask() {
        val module = YamlModule(File("src/test/resources/samplemodule"))
        val type = TaskType("samplemodule", "sample")
        val task = module.getCompositeTask(TaskId(type))
        assertEquals(type, task.descriptor.type)
    }
}