package io.codestream.cli

import io.codestream.core.api.Codestream
import org.junit.jupiter.api.Test

class RunTaskCommandletTest {

    /*
    @Test
    internal fun testRunTaskBaseTask() {
        val tsk = RunTaskCommandlet(Codestream.get(), "sys::echo", mapOf("value" to 1), true)
        tsk.run()
    }*/

    @Test
    internal fun testRunFileBasedTask() {
        val tsk = RunTaskCommandlet(Codestream.get(), "src/test/resources/sample.yaml", emptyMap(), true)
        tsk.run()
    }
}