package io.codestream.util

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EvalTest {

    @Test
    fun testEval() {
        val result = Eval.eval<Boolean>("x > 0", mapOf(Pair("x", 1)), Eval.defaultEngine)
        assertEquals(true, result)
    }


}