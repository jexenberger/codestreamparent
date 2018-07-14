package io.codestream.runtime

import io.codestream.api.Type
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TypeTest {

    @Test
    internal fun testStringType() {
        val type = Type.typeForString("string")
        assertNotNull(type)
        val type2 = Type.typeForString("string[]")
        assertNotNull(type2)
    }


    @Test
    internal fun testTypeString() {
        val type = Type.stringType(String::class)
        assertNotNull(type)
    }

    @Test
    internal fun testTypeForClass() {
        val type = Type.typeForClass(String::class)
        assertNotNull(type)
        assertEquals(Type.string, type?.let { it })
    }

    @Test
    internal fun testConvert() {
        val type = Type.int.convert<Int>("1")
        assertNotNull(type)
        assertEquals(1, type?.let { it })
    }

    @Test
    internal fun testFromString() {
        val type = Type.int.fromString<Int>("1")
        assertNotNull(type)
        assertEquals(1, type.let { it })
    }
}