package io.codestream.core.runtime.modules.system

import io.codestream.core.runtime.ScopedDependencyBindings
import io.codestream.core.runtime.StreamContext
import io.codestream.core.runtime.tree.BranchProcessingDirective
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ForEachTest {

    @Test
    internal fun testBefore() {
        val task = ForEach(arrayOf(1, 2, 3).iterator(), "__var", "__iteratorVar")
        val bindings = StreamContext().bindings
        var next = task.before(bindings)
        assertTrue(bindings.contains("__var"))
        assertTrue(bindings.contains("__iteratorVar"))
        assertEquals(1, bindings["__var"])
        assertEquals(BranchProcessingDirective.continueExecution, next)
        next = task.before(bindings)
        assertEquals(2, bindings["__var"])
        assertEquals(BranchProcessingDirective.continueExecution, next)
        next = task.before(bindings)
        assertEquals(3, bindings["__var"])
        assertEquals(BranchProcessingDirective.continueExecution, next)
        next = task.before(bindings)
        assertEquals(BranchProcessingDirective.done, next)
    }
}