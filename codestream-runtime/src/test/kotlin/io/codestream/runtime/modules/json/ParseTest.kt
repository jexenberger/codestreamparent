package io.codestream.runtime.modules.json

import io.codestream.runtime.StreamContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ParseTest {

    @Test
    internal fun testRun() {
        val bindings = StreamContext().bindings
        val parse = Parse("{ \"hello\": true }")
        val result = parse.evaluate(bindings)!!
        assertEquals(true, result["hello"]!!)
    }


}