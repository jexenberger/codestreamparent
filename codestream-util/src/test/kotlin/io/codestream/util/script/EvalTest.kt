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

package io.codestream.util.script

import io.codestream.util.script.Eval
import org.junit.jupiter.api.Test
import java.io.StringReader
import javax.script.SimpleBindings
import kotlin.test.assertEquals

class EvalTest {

    @Test
    fun testEval() {
        val result = Eval.eval<Boolean>("x > 0", mapOf(Pair("x", 1)), Eval.defaultEngine)
        assertEquals(true, result)
    }

    @Test
    fun testEvalReader() {
        val result = Eval.eval<Boolean>(StringReader("x > 0"), mapOf(Pair("x", 1)), Eval.defaultEngine)
        assertEquals(true, result)
    }


    @Test
    internal fun testCompile() {
        val compiledScript = Eval.compile("x > 0", Eval.defaultEngine)
        val result = compiledScript.eval(SimpleBindings(mapOf("x" to 1)))
        assertEquals(true, result)
    }

    @Test
    internal fun testCompileReader() {
        val compiledScript = Eval.compile(StringReader("x > 0"), Eval.defaultEngine)
        val result = compiledScript.eval(SimpleBindings(mapOf("x" to 1)))
        assertEquals(true, result)
    }
}