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

package io.codestream.util.crypto

import io.codestream.util.system
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class SSLTest {

    @Test
    fun testCtx() {
        val f = "${system.tempDir}/${UUID.randomUUID()}"
        File(f).deleteOnExit()
        val store = SSL.defaultStoreAndKey("localhost", f)
        val ctx = SSL.ctx(store.store, store.store)
        assertNotNull(ctx)
        assertNotNull(store["localhost"])
    }
}