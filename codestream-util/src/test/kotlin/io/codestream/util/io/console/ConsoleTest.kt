package io.codestream.util.io.console

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class ConsoleTest {

    @Test
    internal fun testDisplay() {
        val result = Console.display("hello").display("world").newLine()
        assertNotNull(result)
    }

}