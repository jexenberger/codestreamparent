package io.codestream.runtime.modules.templating

import io.codestream.runtime.StreamContext
import org.junit.jupiter.api.Test
import java.io.StringWriter
import kotlin.test.assertEquals

class GenerateTest {

    @Test
    internal fun testRun() {
        val templatingService = io.codestream.runtime.services.JMustacheTemplatingService()
        val ctx = StreamContext()
        val writer = StringWriter()
        ctx.bindings["test1"] = "hello"
        ctx.bindings["test2"] = "world"
        val generate = Generate("{{test1}} {{test2}}", ctx.bindings, templatingService)
        val result = generate.evaluate(ctx.bindings)
        assertEquals("hello world", result)
    }
}