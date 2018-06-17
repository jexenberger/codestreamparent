package io.codestream.core.runtime

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
        assertEquals(4, entries.size)
        assertNotNull( entries.find { it.key.equals("test1") } )
        assertNotNull( entries.find { it.key.equals("test2") } )
    }

    @Test
    internal fun testKeys() {
        parent["test1"] = "test1"
        bindings["test2"] = "test2"
        val keys = bindings.keys
        //includes implict bindings
        assertEquals(4, keys.size)
        assertTrue { keys.contains("test1") }
        assertTrue { keys.contains("test2") }
    }

    @Test
    internal fun testValues() {
        parent["test1"] = "test1"
        bindings["test2"] = "test2"
        val values = bindings.values
        //includes implict bindings
        assertEquals(4, values.size)
        assertTrue { values.contains("test1") }
        assertTrue { values.contains("test2") }
    }
}