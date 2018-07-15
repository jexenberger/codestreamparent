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
        get.run(bindings)
        assertEquals(200, bindings[get.httpStatusVar])

    }
}