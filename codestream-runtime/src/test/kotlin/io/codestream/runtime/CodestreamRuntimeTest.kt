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

import io.codestream.api.CodestreamSettings
import io.codestream.api.ModuleId
import io.codestream.api.defaultVersion
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.test.assertTrue

class CodestreamRuntimeTest {

    @Test
    internal fun testRunTask() {
        val (result, context) = CodestreamRuntime(CodestreamSettings()).runTask(ModuleId("sys", defaultVersion), "echo", mapOf("value" to "hello world"), DefaultParameterCallback())
        assertTrue { result is Unit }
    }

    @Test
    internal fun testShutdown() {
        val runtime = CodestreamRuntime(CodestreamSettings())
        runtime.runTask(ModuleId("sys", defaultVersion), "echo", mapOf("value" to "hello world"), DefaultParameterCallback())
        //runtime.shutdown()
    }

    @Test
    internal fun testRunGroupTask() {
        CodestreamRuntime(CodestreamSettings()).runTask(ModuleId("sys", defaultVersion), "group", mapOf("value" to "hello world"), DefaultParameterCallback())
    }

    @Test
    internal fun testRunGroupTaskWithCallback() {
        CodestreamRuntime(CodestreamSettings()).runTask(ModuleId("sys", defaultVersion), "echo", emptyMap(), DefaultParameterCallback("red"))
    }

    /*
     need a better test
    @Test
    internal fun testRunGroupTaskWithCallbackEmpty() {
        try {
            CodestreamRuntime(CodestreamSettings()).runTask(ModuleId("sys", defaultVersion), "echo", emptyMap(), DefaultParameterCallback())
            fail("should have failed")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/
}