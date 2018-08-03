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