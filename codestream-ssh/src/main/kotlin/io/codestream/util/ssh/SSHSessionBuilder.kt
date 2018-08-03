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

import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import io.codestream.util.Either
import io.codestream.util.ok
import io.codestream.util.system

class SSHSessionBuilder(val user: String, val host: String, val port: Int = 22) {

    var hostsFile = "${system.homeDir}/.ssh/known_hosts"
    var strictHostChecking: Boolean = true
    var keyFile: Pair<String, String?>? = null
    var password: String = ""
    var timeout: Long = 30000
    var xForwarding: Boolean = false
    var keepAlive: Boolean = false
    var emulation: String? = null
    var prompt: String = "\$"
    var eol: String = "\n"

    val session: Either<SSHSession, String>
        get() = createSession().mapL { SSHSession(it, xForwarding, prompt, eol, emulation) }

    fun hostsFile(file: String): SSHSessionBuilder {
        hostsFile = file
        return this
    }

    fun strictHostChecking(enabled: Boolean): SSHSessionBuilder {
        strictHostChecking = enabled
        return this
    }

    fun keyFile(file: String, passphrase: String? = null): SSHSessionBuilder {
        keyFile = file to passphrase
        return this
    }

    fun password(password: String): SSHSessionBuilder {
        this.password = password
        return this
    }

    fun timeout(timeout: Long): SSHSessionBuilder {
        this.timeout = timeout
        return this
    }

    fun keepAlive(keepAlive: Boolean = true): SSHSessionBuilder {
        this.keepAlive = keepAlive
        return this
    }

    fun emulation(emulation: String): SSHSessionBuilder {
        this.emulation = emulation
        return this
    }

    fun eol(eol: String): SSHSessionBuilder {
        this.eol = eol
        return this
    }

    fun prompt(prompt: String): SSHSessionBuilder {
        this.prompt = prompt
        return this
    }

    fun xForwarding(enabled: Boolean): SSHSessionBuilder {
        this.xForwarding = enabled
        return this
    }

    internal fun createSession(): Either<Session, String> {
        val jsch: JSch = createContext()
        val session = jsch.getSession(user, host, port)
        if (!strictHostChecking) {
            session.setConfig("StrictHostKeyChecking", "no")
        }
        if (keyFile == null && password.isNotBlank()) {
            session.setPassword(password)
        }
        session.connect(timeout.toInt())
        return if (session.isConnected) {
            ok(session)
        } else {
            error("Unable to connect to $host, unknown error")
        }

    }

    //done this way so we don't leak JSch classes to external users
    inline fun <reified T> createContext(): T {
        val jsch = JSch()
        jsch.setKnownHosts(hostsFile)
        keyFile?.let { keyFile ->
            keyFile.second?.let {
                jsch.addIdentity(keyFile.first, it)
            } ?: jsch.addIdentity(keyFile.first)
        }
        return jsch as T
    }


}