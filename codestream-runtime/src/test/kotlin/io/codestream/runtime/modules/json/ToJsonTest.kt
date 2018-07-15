package io.codestream.runtime.modules.json

import io.codestream.runtime.StreamContext
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class ToJsonTest {

    @Test
    internal fun testRun() {
        val bindings = StreamContext().bindings
        val input = mapOf<String, Any?>("hello" to "world")
        val toJson = ToJson(input)
        toJson.run(bindings)
        val result = bindings[toJson.outputVariable] as String?
        assertNotNull(result)
        println(result)
    }
}