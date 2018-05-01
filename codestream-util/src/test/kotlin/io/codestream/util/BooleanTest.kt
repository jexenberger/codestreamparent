package io.codestream.util

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BooleanTest {

    @Test
    fun testWhenTrue() {
        var run = false
        true.whenTrue {
            run = true
        }
        assertTrue { run }
    }

    @Test
    fun testWhenTrueFalse() {
        var run = false
        false.whenTrue {
            run = true
        }
        assertFalse { run }
    }
}