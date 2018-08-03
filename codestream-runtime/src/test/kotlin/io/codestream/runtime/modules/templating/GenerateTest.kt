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

package io.codestream.runtime.modules.templating

import io.codestream.runtime.StreamContext
import org.junit.jupiter.api.Test
import java.io.StringWriter
import kotlin.test.assertEquals

class GenerateTest {

    @Test
    internal fun testRun() {
        val templatingService = io.codestream.runtime.services.MvelTemplateService()
        val ctx = StreamContext()
        ctx.bindings["test1"] = "hello"
        ctx.bindings["test2"] = "world"
        val generate = Generate("@{test1} @{test2}", ctx.bindings, templatingService)
        val result = generate.evaluate(ctx.bindings)
        assertEquals("hello world", result)
    }
}