package io.codestream.util.script

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Jsr223CompilableTest {

    @Test
    internal fun testEvaluate() {
        val script = Eval.compiledScript<Int>("x + 1")
        val value = script.eval(mutableMapOf("x" to 1))
        assertEquals(2, value)
    }
}