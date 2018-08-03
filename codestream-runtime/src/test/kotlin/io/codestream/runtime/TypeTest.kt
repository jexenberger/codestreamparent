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