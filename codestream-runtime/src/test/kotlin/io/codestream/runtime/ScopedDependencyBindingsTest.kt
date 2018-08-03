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

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ScopedDependencyBindingsTest {

    private var streamContext: StreamContext = StreamContext()
    private var bindings: ScopedDependencyBindings = ScopedDependencyBindings(streamContext)
    private var parent: ScopedDependencyBindings = ScopedDependencyBindings(streamContext)

    @BeforeEach
    internal fun setUp() {
        streamContext = StreamContext()
        parent = ScopedDependencyBindings(streamContext)
        bindings = ScopedDependencyBindings(streamContext, parent = parent)
    }

    @Test
    internal fun testSet() {
        bindings.set("test", "test")
        assertEquals("test", bindings["test"])
    }

    @Test
    internal fun testSetParent() {
        parent.set("test", "test")
        assertEquals("test", bindings["test"])
        bindings.set("test2", "test2")
        assertNull(parent["test2"])
    }

    @Test
    internal fun testEntries() {
        parent["test1"] = "test1"
        bindings["test2"] = "test2"
        val entries = bindings.entries
        //includes implict bindings
        assertEquals(6, entries.size)
        assertNotNull( entries.find { it.key.equals("test1") } )
        assertNotNull( entries.find { it.key.equals("test2") } )
    }

    @Test
    internal fun testKeys() {
        parent["test1"] = "test1"
        bindings["test2"] = "test2"
        val keys = bindings.keys
        //includes implict bindings
        assertEquals(6, keys.size)
        assertTrue { keys.contains("test1") }
        assertTrue { keys.contains("test2") }
    }

    @Test
    internal fun testValues() {
        parent["test1"] = "test1"
        bindings["test2"] = "test2"
        val values = bindings.values
        //includes implict bindings
        assertEquals(5, values.size)
        assertTrue { values.contains("test1") }
        assertTrue { values.contains("test2") }
    }
}