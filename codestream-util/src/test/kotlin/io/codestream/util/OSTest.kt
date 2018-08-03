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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OSTest {
    @Test
    fun testOs() {
        //flaky test, how do we check if the right is returned across different platforms
        val os = OS.os()
        assertNotNull(os)
    }

    @Test
    fun testProperties() {
        assertEquals(System.getProperty("os.version"), OS.os().version)
        assertEquals(System.getProperty("os.arch"), OS.os().architecture)
    }

    @Test
    fun testPwd() {
        assertEquals(System.getProperty("user.dir"), OS.os().pwd)
    }

    @Test
    fun testHomeDir() {
        assertEquals(System.getProperty("user.home"), OS.os().homeDir)
    }

    @Test
    fun testPathString() {
        val pathString = OS.os().pathString("hello", "world")
        assertEquals("hello:world", pathString)
    }

    @Test
    fun testFsString() {
        val pathString = OS.os().fsString("hello", "world")
        assertEquals("hello/world", pathString)
    }

    @Test
    fun testFsStringWithRoot() {
        val pathString = OS.os().fsString("hello", "world", absolute = true)
        assertEquals("/hello/world", pathString)
    }

    @Test
    fun testEnvironment() {
        assertNotNull(OS.os().env)
    }

    @Test
    fun testProps() {
        assertNotNull(OS.os().props)
    }

    @Test
    fun testRun() {
        if (OS.os().unixVariant) {
            var called = false
            val res = OS.os().shell("who") {
                called = true
            }
            assertTrue { called }
            assertEquals(0, res)
        }
    }

    @Test
    fun testRunStringOutput() {
        if (OS.os().unixVariant) {
            val (res,str) = OS.os().shell("who")
            assertTrue { str.length > 0 }
            assertEquals(0, res)
            println(str)
        }
    }

}

