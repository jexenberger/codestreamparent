/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.codestream.api

import io.codestream.api.Type
import io.codestream.api.descriptor.ParameterDescriptor
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
        descriptor.isValid("1").right?.let { fail("data passed should be valid") }
    }

    @Test
    internal fun testIsValidUsingType() {
        val descriptor = parameterDescriptor
        descriptor.isValid(1).right?.let { fail("data passed should be valid") }
    }

    @Test
    internal fun testIsInValidWhenRequired() {
        val descriptor = parameterDescriptor
        descriptor.isValid(null).right?.let { assertEquals(ErrorType.required, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }

    @Test
    internal fun testIsInValidWhenInvalidType() {
        val descriptor = parameterDescriptor
        descriptor.isValid("qwerty").right?.let { assertEquals(ErrorType.invalidType, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }

    @Test
    internal fun testIsInValidRegex() {
        val descriptor = parameterRegex
        descriptor.isValid("ac").right?.let { assertEquals(ErrorType.invalidFormat, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }

    @Test
    internal fun testIsValidRegex() {
        val descriptor = parameterRegex
        descriptor.isValid("ab").right?.let { fail("should have passed validation") }
    }

    @Test
    internal fun testIsAllowedValue() {
        val descriptor = parameterListOfValue
        descriptor.isValid("one").right?.let { fail("should have passed validation") }
    }

    @Test
    internal fun testIsNotAllowedValue() {
        val descriptor = parameterListOfValue
        descriptor.isValid("three").right?.let { assertEquals(ErrorType.invalidValue, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }
    @Test
    internal fun testIsNotAllowedValueMap() {
        val candidate = mapOf("four" to "test")
        val descriptor = parameterListOfValue
        descriptor.isValid(candidate).right?.let { assertEquals(ErrorType.invalidValue, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }

    @Test
    internal fun testIsNotAllowedValueArray() {
        val candidate = arrayOf("four")
        val descriptor = parameterListOfValue
        descriptor.isValid(candidate).right?.let { assertEquals(ErrorType.invalidValue, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }

    @Test
    internal fun testIsNotAllowedValueList() {
        val candidate = listOf("four")
        val descriptor = parameterListOfValue
        descriptor.isValid(candidate).right?.let { assertEquals(ErrorType.invalidValue, it.validationErrors.iterator().next().type) }
                ?: fail("should have failed")
    }

}