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

import io.codestream.util.transformation.TransformerService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class SecretTest {


    private val key = SystemKey.get()
    private val handler = AES()


    @Test
    fun testDecrypt() {

        val encrypt = handler.encrypt("hello world".toByteArray(), key.encoded)
        val decrypt = Secret.decrypt(encrypt, key, handler)
        assertEquals("hello world", String(decrypt))

    }

    @Test
    fun testEncrypt() {
        val encrypt = handler.encrypt("hello world".toByteArray(), key.encoded)
        val check = Secret.encrypt("hello world".toByteArray(), key, handler)
        assertEquals(String(encrypt), String(check))
    }

    @Test
    fun testCipherTextBase64() {
        val encrypt = Base64.getEncoder().encodeToString(handler.encrypt("hello world".toByteArray(), key.encoded))
        val secret = Secret("hello world", key)
        assertEquals(encrypt, secret.cipherTextBase64)
    }

    @Test
    fun testToAndFromString() {
        val secret = TransformerService.convert<Secret>("hello world")
        val plainText = TransformerService.convert<String>(secret)
        assertEquals("hello world", plainText)
    }
}