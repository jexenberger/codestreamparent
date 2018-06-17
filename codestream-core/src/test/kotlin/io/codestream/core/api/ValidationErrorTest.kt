package io.codestream.core.api

import io.codestream.core.runtime.Type
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ValidationErrorTest {


    @Test
    internal fun testRequired() {
        val required = ValidationError().required("test")
        assertEquals(ErrorType.required, required.errors.iterator().next().type)
    }

    @Test
    internal fun invalidValueForList() {
        val required = ValidationError().invalidValueForList("test", arrayOf("one", "two"))
        assertEquals(ErrorType.invalidValue, required.errors.iterator().next().type)
    }

    @Test
    internal fun invalidFormat() {
        val required = ValidationError().invalidFormat("test", "xxx")
        assertEquals(ErrorType.invalidFormat, required.errors.iterator().next().type)
    }

    @Test
    internal fun invalidType() {
        val required = ValidationError().invalidType("test", Type.string)
        assertEquals(ErrorType.invalidType, required.errors.iterator().next().type)
    }

    @Test
    internal fun notSupported() {
        val required = ValidationError().notSupported("test", "not supported")
        assertEquals(ErrorType.notSupported, required.errors.iterator().next().type)
    }
}