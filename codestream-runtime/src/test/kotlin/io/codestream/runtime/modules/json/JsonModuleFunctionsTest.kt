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

package io.codestream.runtime.modules.json

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JsonModuleFunctionsTest {

    @Test
    internal fun testArrayToJson() {
        val json = JsonModuleFunctions().arrayToJson(arrayOf(1, 2, 3))
        assertEquals("[ 1, 2, 3 ]", json)
    }

    @Test
    internal fun testArrayToJsonString() {
        val json = JsonModuleFunctions().arrayToJson(arrayOf("hello", "world"))
        assertEquals("[ \"hello\", \"world\" ]", json)
    }

    @Test
    internal fun testCollectionToJson() {
        val json = JsonModuleFunctions().collectionToJson(listOf(1, 2, 3))
        assertEquals("[ 1, 2, 3 ]", json)
    }
}