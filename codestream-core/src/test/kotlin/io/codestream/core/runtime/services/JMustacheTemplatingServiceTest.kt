package io.codestream.core.runtime.services

import io.codestream.core.runtime.StreamContext
import org.junit.jupiter.api.Test
import java.io.StringReader
import java.io.StringWriter
import kotlin.test.assertEquals

class JMustacheTemplatingServiceTest {

    @Test
    internal fun testWrite() {
        val templatingService = JMustacheTemplatingService()
        val ctx = StreamContext()
        val writer = StringWriter()
        ctx.bindings["test1"] = "hello"
        ctx.bindings["test2"] = "world"
        templatingService.write(StringReader("{{test1}} {{test2}}"), writer, ctx)
        assertEquals("hello world", writer.toString())
    }
}