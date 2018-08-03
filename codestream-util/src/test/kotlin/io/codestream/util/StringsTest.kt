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

package io.codestream.util

import org.junit.jupiter.api.Test
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class StringsTest {
    @Test
    fun testIsEmpty() {
        assertTrue(Strings.isEmpty(""))
        assertTrue(Strings.isEmpty(" "))
        assertTrue(Strings.isEmpty(null))
        assertFalse(Strings.isEmpty("hello"))
    }

    @Test
    fun testExec() {
        if (OS.os().unixVariant) {
            val result = "ls -l".exec()
            assertEquals(0, result.first)
            assertNotNull(result.second)
            println(result.second)
        } else {
            val result = "C:\\Windows\\System32\\where.exe".exec()
            assertEquals(2, result.first)
            assertNotNull(result.second)
            println(result.second)
        }

    }


    @Test
    fun testExecTimeout() {
        if (OS.os().unixVariant) {
            val result = "sleep 2".exec(File(System.getProperty("user.dir")), 1, TimeUnit.SECONDS)
            assertEquals(-1, result.first)
            assertNotNull(result.second)
            println(result.second)
        }
    }
}