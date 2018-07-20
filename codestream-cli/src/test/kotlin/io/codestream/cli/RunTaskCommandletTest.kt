package io.codestream.cli

import io.codestream.api.CodestreamFactory
import org.junit.jupiter.api.Test

class RunTaskCommandletTest {

    /*
    @Test
    internal fun testRunTaskBaseTask() {
        val tsk = RunTaskCommandlet(Codestream.get(), "sys::echo", mapOf("value" to 1), true)
        tsk.evaluate()
    }*/

    @Test
    internal fun testRunFileBasedTask() {
        val tsk = RunTaskCommandlet(CodestreamFactory.get().get(), "src/test/resources/sample.yaml", emptyMap(), true)
        tsk.run()
    }
}