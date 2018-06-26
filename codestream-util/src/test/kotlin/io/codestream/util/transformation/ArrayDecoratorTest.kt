package io.codestream.util.transformation

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ArrayDecoratorTest {

    @Test
    internal fun testConstructor() {
        ArrayDecorator<String>(arrayOf("hello", "world"))
        ArrayDecorator<Int>(arrayOf(1, 2))
        ArrayDecorator<IntArray>(arrayOf(1, 2))
    }

    @Test
    internal fun testSet() {
        val stringDecorator = ArrayDecorator<String>(arrayOf("hello", "world"))
        stringDecorator.set(1, "hello")
        assertEquals("hello", stringDecorator[1])
        val intDecorator = ArrayDecorator<Int>(arrayOf(1, 2))
        intDecorator[1] = 99
        assertEquals(99, intDecorator[1])
        val instance = IntArray(2)
        instance[0]  = 1
        instance[1] = 2

        val arrayDecorator = ArrayDecorator<Int>(instance)
        arrayDecorator[1] = 999
        assertEquals(999, instance[1])
    }
}