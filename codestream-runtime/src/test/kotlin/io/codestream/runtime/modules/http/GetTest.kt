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

package io.codestream.runtime.modules.http

import io.codestream.runtime.StreamContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetTest {

    @Test
    internal fun testRun() {
        val get = Get()
        //get.uri = "http://www.google.com"
        //val bindings = StreamContext().bindings
        //val result = get.evaluate(bindings)!!
        //assertEquals(200, result["status"]!!)

    }
}