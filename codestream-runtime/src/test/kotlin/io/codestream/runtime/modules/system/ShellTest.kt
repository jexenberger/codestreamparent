package io.codestream.runtime.modules.system

import io.codestream.runtime.StreamContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShellTest {

    @Test
    internal fun testRun() {
        val shell = Shell("ls -lh|xargs echo" , outputVariable = "_out", resultCodeVariable = "_ret")
        val bindings = StreamContext().bindings
        shell.run(bindings)
        val ret = bindings["_ret"]
        val buffer = bindings["_out"]
        assertEquals(0, ret)
        assertTrue { buffer != null && buffer.toString().length > 0 }
        println(ret)

    }
}