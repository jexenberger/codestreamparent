package io.codestream.runtime.modules.json

import io.codestream.runtime.StreamContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ParseTest {

    @Test
    internal fun testRun() {
        val bindings = StreamContext().bindings
        val parse = Parse("{ \"hello\": true }")
        parse.run(bindings)
        val result = bindings[parse.outputVariable] as Map<String, Any?>
        assertEquals(true, result["hello"])
    }
}