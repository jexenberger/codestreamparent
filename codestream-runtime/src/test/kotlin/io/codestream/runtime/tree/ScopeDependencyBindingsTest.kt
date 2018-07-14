package io.codestream.runtime.tree

import io.codestream.di.api.DefaultApplicationContext
import io.codestream.runtime.ScopedDependencyBindings
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class ScopeDependencyBindingsTest {

    @Test
    internal fun testCreate() {
        val bindings = ScopedDependencyBindings(
                DefaultApplicationContext(),
                mutableMapOf(),
                null
        )
        assertNotNull(bindings)
    }
}