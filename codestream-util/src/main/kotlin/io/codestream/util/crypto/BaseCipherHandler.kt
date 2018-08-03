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

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

interface BaseCipherHandler : CipherHandler {

    override fun generateKey() = generateSecretKey()
    override fun generateSecretKey() = KeyGenerator.getInstance(algorithm).generateKey()

    override fun encrypt(plainText: ByteArray, key: ByteArray): ByteArray {
        val secretKey = SecretKeySpec(key, algorithm)
        return encrypt(plainText, secretKey)
    }

    override fun encrypt(plainText: ByteArray, secretKey: Key): ByteArray {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher.doFinal(plainText)
    }

    override fun decrypt(cipherText: ByteArray, key: ByteArray): ByteArray {
        val secretKey = SecretKeySpec(key, algorithm)
        return decrypt(secretKey, cipherText)
    }

    override fun decrypt(secretKey: Key, cipherText: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return cipher.doFinal(cipherText)
    }
}