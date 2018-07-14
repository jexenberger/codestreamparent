package io.codestream.core.engine

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DefaultBranchTest {

    @Test
    internal fun testExecute() {
        var called2 = false
        var visited2 = false
        var visited2After = false
        var called3 = false
        var called4 = false
        val root = DefaultBranch(
                id = "1",
                parallel =  false,
                beforeLeafVisitor = {leaf, ctx -> if (leaf.id.equals("2")) visited2 = true },
                afterLeafVisitor = {state, leaf, ctx -> if (state == NodeState.completed && leaf.id.equals("2")) visited2After = true })
        root += DefaultLeaf("2") { called2 = true }
        root += DefaultLeaf("3") { called3 = true }
        root += DefaultLeaf("4") { called4 = true }
        val result = root.execute(DefaultExecutionContext())
        assertEquals(NodeState.completed, result)
        assertTrue { called2 }
        assertTrue { called3 }
        assertTrue { called4 }
        assertTrue { visited2 }
        assertTrue { visited2After }
    }

    @Test
    internal fun testExecuteWithFailurePostTraversal() {
        var called2 = false
        var called3 = false
        var errorCalled = true
        val root = DefaultBranch(
                id = "1",
                parallel = false,
                postTraversal = { throw IllegalStateException("borked")},
                onError = { e, ctx ->
                    assertTrue { e is IllegalStateException }
                    errorCalled = true
                }

        )
        root += DefaultLeaf("2") { called2 = true }
        root += DefaultLeaf("3") { called3 = true }
        val result = root.execute(DefaultExecutionContext())
        assertEquals(NodeState.failed, result)
        assertTrue { called2 }
        assertTrue { called3 }
        assertTrue { errorCalled }
    }

    @Test
    internal fun testExecuteWithFailurePreTraversal() {
        var called2 = false
        var errorCalled = true
        val root = DefaultBranch(
                id = "1",
                parallel = false,
                preTraversal = { throw IllegalStateException("borked")},
                onError = { e, ctx ->
                    assertTrue { e is IllegalStateException }
                    errorCalled = true
                }

        )
        root += DefaultLeaf("2") { called2 = true }
        val result = root.execute(DefaultExecutionContext())
        assertEquals(NodeState.failed, result)
        assertFalse { called2 }
        assertTrue { errorCalled }
    }
    @Test
    internal fun testExecuteWithFailureOnNode() {
        var errorCalled = true
        var leafErrorCalled = true
        val root = DefaultBranch(
                id = "1",
                parallel = false,
                leafErrorVisitor = { e, node, ctx -> leafErrorCalled = true},
                onError = { e, ctx ->
                    assertTrue { e is IllegalStateException }
                    errorCalled = true
                }

        )
        root += DefaultLeaf("2") { throw IllegalStateException("borked") }
        val result = root.execute(DefaultExecutionContext())
        assertEquals(NodeState.failed, result)
        assertTrue { errorCalled }
        assertTrue { leafErrorCalled }
    }

    @Test
    internal fun testNodeById() {
        val root = DefaultBranch("root", false)
        val level2 = DefaultBranch("level2", false)
        level2 += DefaultLeaf("l21", {})
        val lookupNode = DefaultLeaf("l22", {})
        level2 += lookupNode
        root += DefaultLeaf("1", {})
        root += DefaultLeaf("3", {})
        root += level2

        val foundNode = root.nodeById("l22")
        assertNotNull(foundNode)
        assertSame(lookupNode, foundNode)


    }
}