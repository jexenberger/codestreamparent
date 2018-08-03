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

package io.codestream.util.io

import io.codestream.util.system
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IOFunctionsTest {
    lateinit var one: String
    lateinit var two: String
    lateinit var dir1: File
    lateinit var dir2: File
    lateinit var files: List<String>
    lateinit var testDir: String
    lateinit var copyDir: String

    @BeforeEach
    fun setUp() {
        one = UUID.randomUUID().toString()
        two = UUID.randomUUID().toString()
        testDir = "${system.homeDir}/.cstest"
        files = listOf(
                "$testDir/$one/a.txt",
                "$testDir/$two/a.txt",
                "$testDir/$one/a.xxx",
                "$testDir/$two/a.xxx"
        )
        dir1 = File("$testDir", one)
        dir2 = File("$testDir", two)
        dir1.mkdirs()
        dir2.mkdirs()

        files.forEach {
            File(it).writeText("test")
        }
        copyDir = "$testDir/copytarget"
    }

    @Test
    fun testFileList() {
        val funcs = IOFunctions

        val find = funcs.find(testDir, "**/a.txt")
        assertEquals(2, find.size)
        find.forEach {
            assertTrue { it.endsWith("a.txt") }
        }
    }

    @Test
    fun testCopyDir() {
        val funcs = IOFunctions
        funcs.copyDir(testDir, copyDir, "**/a.txt")
        val find = funcs.find(copyDir, "**/*.*")
        assertEquals(2, find.size)
        find.forEach {
            assertTrue { it.endsWith("a.txt") }
        }
    }

    @AfterEach
    fun tearDown() {
        dir1.deleteRecursively()
        dir2.deleteRecursively()

        files.forEach {
            Files.deleteIfExists(File(it).toPath())
        }
        Files.deleteIfExists(dir1.toPath())
        Files.deleteIfExists(dir2.toPath())

        val copyPath = File(copyDir)
        copyPath.deleteRecursively()
        Files.deleteIfExists(copyPath.toPath())
    }
}