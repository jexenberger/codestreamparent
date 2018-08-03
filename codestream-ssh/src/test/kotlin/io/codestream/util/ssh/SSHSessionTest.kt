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

package io.codestream.util.ssh

import io.codestream.util.system
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class SSHSessionTest : BaseSSHTest() {

    @Test
    fun testExec() {

        MockSSHServer.stop()
        MockSSHServer.start()

        val session = SSHSessionBuilder(settings.sshUser, settings.sshHost, port = 2022)
                .password(settings.sshPassword)
                .hostsFile(settings.sshKnownHosts)
                .strictHostChecking(false)
                .session
        session.left?.exec("ls") { line ->
            println(line)
        } ?: fail(session.right)
        session.mapL { it.disconnect() }

    }

    @Test
    fun testShell() {

        val session = SSHSessionBuilder(settings.sshUser, settings.sshHost, port = 2022)
                .password(settings.sshPassword)
                .hostsFile(settings.sshKnownHosts)
                .keepAlive()
                .strictHostChecking(false)
                .eol("\n")
                .prompt("\$")
                .emulation("vt100")
                .session

        session.left?.run {
            shell("export TESTING=123;ls") { line ->
                println(line)
            }
            shell("export TESTING=123;ls") { line ->
                println(line)
            }
            shell("echo \$TESTING") { line ->
                assertEquals("123", line)
            }
        }

    }


    @Test
    fun testScpTo() {
        val file = File.createTempFile("ssh", "test")
        for (i in 1..100000) {
            file.appendText("hello world")
        }
        val session = SSHSessionBuilder(settings.sshUser, settings.sshHost, port = 2022)
                .password(settings.sshPassword)
                .strictHostChecking(false)
                .session
        val result = session.mapL { it.scpTo(file.absolutePath, system.homeDir) ?: "success" }
        assertTrue(result.left != null, result.right)
        session.mapL { it.disconnect() }
    }

    @Test
    fun testScpFrom() {
        val file = File.createTempFile("ssh", "test")
        file.appendText("hello world")
        val builder = SSHSessionBuilder(settings.sshUser, settings.sshHost, port = 2022)
                .strictHostChecking(false)
                .password(settings.sshPassword)
        var session = builder
                .session
        var scpFile = settings.sshScpFile
        println(scpFile)
        val result = session.mapL {
            it.scpFrom(system.tempDir, scpFile) ?: "success"
        }
        val baseName = File(scpFile).name
        assertTrue(result.left != null, result.right)
        assertTrue { File("${system.tempDir}/$baseName").exists() }
        session.mapL { it.disconnect() }
    }
}