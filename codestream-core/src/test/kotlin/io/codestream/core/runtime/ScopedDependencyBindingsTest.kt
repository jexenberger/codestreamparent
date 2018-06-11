package io.codestream.core.runtime

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ScopedDependencyBindingsTest {

    @Test
    internal fun testSet() {
        val bindings = ScopedDependencyBindings(StreamContext())
        bindings.set("test", "test")
        assertEquals("test", bindings["test"])
    }

    @Test
    internal fun testSetParent() {
        val ctx = StreamContext()
        val parent = ScopedDependencyBindings(ctx)
        val bindings = ScopedDependencyBindings(ctx, parent = parent)
        parent.set("test", "test")
        assertEquals("test", bindings["test"])
    }
}