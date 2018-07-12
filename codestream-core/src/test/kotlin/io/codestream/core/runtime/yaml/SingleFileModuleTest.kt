package io.codestream.core.runtime.yaml

import io.codestream.core.api.ModuleId
import io.codestream.core.api.TaskType
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SingleFileModuleTest {

    private val standaloneFile = File("src/test/resources/standalone/standalonetask.yaml")
    private  val module = SingleFileModule(standaloneFile)

    @Test
    internal fun testTaskDescription() {
        val result = module.get(TaskType(ModuleId.fromString(standaloneFile.parentFile.absolutePath), "standalone"))
        assertNotNull(result)
        println(result.type)
    }

    @Test
    internal fun testName() {

        assertEquals(standaloneFile.parentFile.absolutePath, module.name)
    }
    @Test
    internal fun testDescription() {
        assertEquals("This is a simple standalone task", module.description)
    }
}