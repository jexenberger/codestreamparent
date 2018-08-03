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