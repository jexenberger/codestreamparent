package io.codestream.util.crypto

import io.codestream.util.system
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class JavaKeyStoreTest {


    @Test
    fun testAddKeyPairCertificate() {
        val cert = SSLCertificate(
                cn = "localhost",
                ou = "codestream",
                org = "codestream",
                locality = "nowhereia",
                state = "nowhereiastate"
        )
        val file = File("${system.tempDir}/${UUID.randomUUID()}")
        file.deleteOnExit()
        val store = JavaKeyStore(file, "changeit")
        val (keyPair, certificate) = cert.selfSignedCertificate
        store.addKeyAndCertificate("test", keyPair, certificate)
        assertTrue { file.exists() }

        val newStore = JavaKeyStore(file, "changeit")
        assertNotNull(newStore["test"])
        println(newStore["test"])

    }
}