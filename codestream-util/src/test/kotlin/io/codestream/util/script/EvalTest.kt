package io.codestream.util.script

import io.codestream.util.script.Eval
import org.junit.jupiter.api.Test
import java.io.StringReader
import javax.script.SimpleBindings
import kotlin.test.assertEquals

class EvalTest {

    @Test
    fun testEval() {
        val result = Eval.eval<Boolean>("x > 0", mapOf(Pair("x", 1)), Eval.defaultEngine)
        assertEquals(true, result)
    }

    @Test
    fun testEvalReader() {
        val result = Eval.eval<Boolean>(StringReader("x > 0"), mapOf(Pair("x", 1)), Eval.defaultEngine)
        assertEquals(true, result)
    }


    @Test
    internal fun testCompile() {
        val compiledScript = Eval.compile("x > 0", Eval.defaultEngine)
        val result = compiledScript.eval(SimpleBindings(mapOf("x" to 1)))
        assertEquals(true, result)
    }

    @Test
    internal fun testCompileReader() {
        val compiledScript = Eval.compile(StringReader("x > 0"), Eval.defaultEngine)
        val result = compiledScript.eval(SimpleBindings(mapOf("x" to 1)))
        assertEquals(true, result)
    }
}