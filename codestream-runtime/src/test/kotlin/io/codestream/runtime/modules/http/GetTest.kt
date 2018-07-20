package io.codestream.runtime.modules.http

import io.codestream.runtime.StreamContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetTest {

    @Test
    internal fun testRun() {
        val get = Get()
        get.uri = "http://www.google.com"
        val bindings = StreamContext().bindings
        val result = get.evaluate(bindings)!!
        assertEquals(200, result["status"]!!)

    }
}