package io.codestream.runtime.modules.system

import io.codestream.runtime.StreamContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShellTest {

    @Test
    internal fun testRun() {
        val shell = Shell("ls -lh|xargs echo" )
        val bindings = StreamContext().bindings
        val result = shell.evaluate(bindings)
        val ret = result?.get("result")
        val buffer = result?.get("output")
        assertEquals(0, ret)
        assertTrue { buffer != null && buffer.toString().length > 0 }
        println(ret)

    }
}