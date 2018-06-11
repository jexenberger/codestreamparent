package io.codestream.core.runtime.tree

import io.codestream.core.runtime.ScopedDependencyBindings
import io.codestream.di.runtime.DefaultApplicationContext
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