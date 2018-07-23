package io.codestream.util.ssh

import io.codestream.util.system

data class SSHServer(
        val sshUser: String = "test",
        val sshHost: String = "localhost",
        val sshPassword: String = "test!",
        val sshKnownHosts: String = "${system.homeDir}./ssh/known_hosts",
        val sshScpFile: String = "src/test/resources/scp.txt") {
}