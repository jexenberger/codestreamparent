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

import de.skuzzle.semantic.Version
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModuleIdTest {

    @Test
    internal fun testFromString() {
        val id = ModuleId.fromString("hello")
        assertEquals("hello", id.name)
        assertEquals(defaultVersion, id.version)
    }

    @Test
    internal fun testFromStringWithVersion() {
        val id = ModuleId.fromString("hello@1.0.0")
        assertEquals("hello", id.name)
        assertEquals(Version.create(1, 0, 0), id.version)
    }
}