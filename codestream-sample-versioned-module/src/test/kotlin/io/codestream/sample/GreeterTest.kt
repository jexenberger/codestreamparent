package io.codestream.sample

import io.codestream.api.CodestreamFactory
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GreeterTest {

    @Test
    internal fun testRun() {
        val greeter = Greeter("Jack")
        val ctx = CodestreamFactory.get().runContext()
        greeter.run(ctx)
        assertEquals("Jack", ctx["greeted"])
    }
}