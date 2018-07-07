package io.codestream.sample

import io.codestream.core.api.Codestream
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GreeterTest {

    @Test
    internal fun testRun() {
        val greeter = Greeter("Jack")
        val ctx = Codestream.runContext()
        greeter.run(ctx)
        assertEquals("Jack", ctx["greeted"])
    }
}