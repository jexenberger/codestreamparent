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

package io.codestream.runtime.tree

import io.codestream.api.Directive
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.Executors
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
        var skipped = false
        val root = DefaultBranch<String>(
                id = "1",
                parallel = false)
        root += DefaultLeaf<String>("2",
                beforeLeafVisitor = { n, c -> visited2 = true ;true},
                skippedVisitor = { n, c -> skipped = true},
                afterLeafVisitor = { n, c, s -> visited2After = true },
                handler = { called2 = true }
        )
        root += DefaultLeaf("3") { called3 = true }
        root += DefaultLeaf("4") { called4 = true }
        val result = root.execute("hello")
        assertEquals(NodeState.completed, result)
        assertTrue { called2 }
        assertFalse { skipped }
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
        val root = DefaultBranch<String>(
                id = "1",
                parallel = false,
                postTraversal = { throw IllegalStateException("borked") },
                onError = { e, ctx ->
                    assertTrue { e is IllegalStateException }
                    errorCalled = true
                }

        )
        root += DefaultLeaf("2") { called2 = true }
        root += DefaultLeaf("3") { called3 = true }
        val result = root.execute("hello")
        assertEquals(NodeState.failed, result)
        assertTrue { called2 }
        assertTrue { called3 }
        assertTrue { errorCalled }
    }

    @Test
    internal fun testExecuteWithFailurePreTraversal() {
        var called2 = false
        var errorCalled = true
        val root = DefaultBranch<String>(
                id = "1",
                parallel = false,
                preTraversal = { throw IllegalStateException("borked") },
                onError = { e, ctx ->
                    assertTrue { e is IllegalStateException }
                    errorCalled = true
                }
        )
        root += DefaultLeaf("2") { called2 = true }
        val result = root.execute("hello")
        assertEquals(NodeState.failed, result)
        assertFalse { called2 }
        assertTrue { errorCalled }
    }

    @Test
    internal fun testExecuteWithFailureOnNode() {
        var errorCalled = true
        var leafErrorCalled = true
        val root = DefaultBranch<String>(
                id = "1",
                parallel = false,
                onError = { e, ctx ->
                    assertTrue { e is IllegalStateException }
                    errorCalled = true
                }
        )
        root += DefaultLeaf("2", handler = { throw IllegalStateException("borked") })
        val result = root.execute("hello")
        assertEquals(NodeState.failed, result)
        assertTrue { errorCalled }
        assertTrue { leafErrorCalled }
    }

    @Test
    internal fun testNodeById() {
        val root = DefaultBranch<String>("root", false)
        val level2 = DefaultBranch<String>("level2", false)
        level2 += DefaultLeaf("l21", {})
        val lookupNode = DefaultLeaf<String>("l22", {})
        level2 += lookupNode
        root += DefaultLeaf("1", {})
        root += DefaultLeaf("3", {})
        root += level2

        val foundNode = root.nodeById("l22")
        assertNotNull(foundNode)
        assertSame(lookupNode, foundNode)
    }

    @Test
    internal fun testLoopingExecution() {
        var cnt = 0;
        val root = DefaultBranch<String>(
                id = "1",
                parallel = false,
                postTraversal = { ctx ->
                    if (cnt < 3) {
                        cnt += 1
                        Directive.again
                    } else {
                        Directive.done
                    }
                }
        )

        val result = root.execute("hello")
        assertEquals(3, cnt)
    }

    @Test
    internal fun testParallellLoopingExecution() {
        var cnt = 0;
        val unordered = Collections.synchronizedCollection(mutableListOf<Int>())
        val root = DefaultBranch<String>(
                id = "1",
                parallel = true,
                postTraversal = { ctx ->
                    if (unordered.size < 3) {
                        unordered += 1
                        if (unordered.size % 2 == 0) {
                            Thread.sleep(1000)
                        }
                        Directive.again
                    } else {
                        Directive.done
                    }
                }
        )

        root.execute("hello")
        assertEquals(3, unordered.size)
        println(unordered)
    }
}