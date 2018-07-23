package io.codestream.util.ssh

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class BaseSSHTest {

    val settings:SSHServer = SSHServer()


    @BeforeEach
    fun setUp() {

        MockSSHServer.start(true)
    }

    @AfterEach
    fun tearDown() {
        MockSSHServer.stop()
    }
}