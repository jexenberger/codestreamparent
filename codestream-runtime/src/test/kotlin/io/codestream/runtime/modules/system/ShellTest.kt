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

package io.codestream.runtime.modules.system

import io.codestream.runtime.StreamContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShellTest {

    @Test
    internal fun testRun() {
        val shell = Shell("ls -lh|xargs echo" )
        val bindings = StreamContext().bindings
        val result = shell.evaluate(bindings)
        val ret = result?.get("result")
        val buffer = result?.get("output")
        assertEquals(0, ret)
        assertTrue { buffer != null && buffer.toString().length > 0 }
        println(ret)

    }
}