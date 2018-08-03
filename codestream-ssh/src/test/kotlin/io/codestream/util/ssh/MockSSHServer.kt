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
import org.apache.sshd.common.keyprovider.FileKeyPairProvider
import org.apache.sshd.common.keyprovider.KeyPairProvider
import org.apache.sshd.server.SshServer
import org.apache.sshd.server.auth.password.PasswordAuthenticator
import org.apache.sshd.server.command.CommandFactory
import org.apache.sshd.server.config.keys.DefaultAuthorizedKeysAuthenticator
import org.apache.sshd.server.scp.ScpCommandFactory
import org.apache.sshd.server.shell.ProcessShellFactory
import java.io.File
import java.nio.file.Paths
import java.security.KeyPair


object MockSSHServer {


    private var server: SshServer? = null


    fun start(scp: Boolean = false) {
        val paths = File(system.homeDir, ".ssh").list().map { Paths.get(system.homeDir, ".ssh", it) }
        server = SshServer.setUpDefaultServer()
        server?.port = 2022
        server?.keyPairProvider = FileKeyPairProvider(paths)
        server?.publickeyAuthenticator = DefaultAuthorizedKeysAuthenticator(false)
        server?.passwordAuthenticator = PasswordAuthenticator { user, pwd, session ->
            "test".equals(user) && "test!".equals(pwd)
        }
        server?.host = "localhost"
        server?.start()
        if (!scp) {
            server?.commandFactory = CommandFactory { cmd ->
                ProcessShellFactory(cmd).create()
            }
        } else {
            server?.commandFactory = ScpCommandFactory()
        }
    }

    fun stop() {
        server?.stop(true)
    }

}

class MockKeyPairProvider : KeyPairProvider {
    override fun loadKeys(): MutableIterable<KeyPair> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}