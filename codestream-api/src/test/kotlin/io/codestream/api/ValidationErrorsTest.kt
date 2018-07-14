package io.codestream.api

import io.codestream.core.runtime.Type
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ValidationErrorsTest {


    @Test
    internal fun testRequired() {
        val required = ValidationErrors().required("test")
        assertEquals(ErrorType.required, required.validationErrors.iterator().next().type)
    }

    @Test
    internal fun invalidValueForList() {
        val required = ValidationErrors().invalidValueForList("test", arrayOf("one", "two"))
        assertEquals(ErrorType.invalidValue, required.validationErrors.iterator().next().type)
    }

    @Test
    internal fun invalidFormat() {
        val required = ValidationErrors().invalidFormat("test", "xxx")
        assertEquals(ErrorType.invalidFormat, required.validationErrors.iterator().next().type)
    }

    @Test
    internal fun invalidType() {
        val required = ValidationErrors().invalidType("test", Type.string)
        assertEquals(ErrorType.invalidType, required.validationErrors.iterator().next().type)
    }

    @Test
    internal fun notSupported() {
        val required = ValidationErrors().notSupported("test", "not supported")
        assertEquals(ErrorType.notSupported, required.validationErrors.iterator().next().type)
    }
}