package io.codestream.api

import io.codestream.core.api.descriptor.ParameterDescriptor
import io.codestream.core.runtime.Type
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ParameterDescriptorTest {

    private val parameterDescriptor = ParameterDescriptor("test", "test desc", Type.int)
    private val parameterRegex = ParameterDescriptor("test", "test desc", Type.string, regex = "ab+")
    private val parameterListOfValue = ParameterDescriptor("test", "test desc", Type.string, allowedValues = arrayOf("one", "two"))

    @Test
    internal fun testIsValid() {
        val descriptor = parameterDescriptor
        descriptor.isValid("1")?.let { fail("data passed should be valid") }
    }

    @Test
    internal fun testIsValidUsingType() {
        val descriptor = parameterDescriptor
        descriptor.isValid(1)?.let { fail("data passed should be valid") }
    }

    @Test
    internal fun testIsInValidWhenRequired() {
        val descriptor = parameterDescriptor
        descriptor.isValid(null)?.let { assertEquals(ErrorType.required, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }

    @Test
    internal fun testIsInValidWhenInvalidType() {
        val descriptor = parameterDescriptor
        descriptor.isValid("qwerty")?.let { assertEquals(ErrorType.invalidType, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }

    @Test
    internal fun testIsInValidRegex() {
        val descriptor = parameterRegex
        descriptor.isValid("ac")?.let { assertEquals(ErrorType.invalidFormat, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }

    @Test
    internal fun testIsValidRegex() {
        val descriptor = parameterRegex
        descriptor.isValid("ab")?.let { fail("should have passed validation") }
    }

    @Test
    internal fun testIsAllowedValue() {
        val descriptor = parameterListOfValue
        descriptor.isValid("one")?.let { fail("should have passed validation") }
    }

    @Test
    internal fun testIsNotAllowedValue() {
        val descriptor = parameterListOfValue
        descriptor.isValid("three")?.let { assertEquals(ErrorType.invalidValue, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }
    @Test
    internal fun testIsNotAllowedValueMap() {
        val candidate = mapOf("four" to "test")
        val descriptor = parameterListOfValue
        descriptor.isValid(candidate)?.let { assertEquals(ErrorType.invalidValue, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }

    @Test
    internal fun testIsNotAllowedValueArray() {
        val candidate = arrayOf("four")
        val descriptor = parameterListOfValue
        descriptor.isValid(candidate)?.let { assertEquals(ErrorType.invalidValue, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }

    @Test
    internal fun testIsNotAllowedValueList() {
        val candidate = listOf("four")
        val descriptor = parameterListOfValue
        descriptor.isValid(candidate)?.let { assertEquals(ErrorType.invalidValue, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }

}