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

import java.util.*
import javax.crypto.SecretKey

data class Secret(val cipherText: ByteArray, val key: SecretKey = SystemKey.get(), private val cipherHandler: CipherHandler = AES()) {


    constructor(plainText: String,
                key: SecretKey = SystemKey.get(),
                cipherHandler: CipherHandler = AES())
            : this(cipherHandler.encrypt(plainText.toByteArray(), key), key, cipherHandler)


    val cipherTextBase64: String
        get() = Base64.getEncoder().encodeToString(cipherText)

    val value: String
        get() = String(decrypt(value = cipherText, keyFile = key, handler = cipherHandler))


    override fun toString(): String {
        return cipherTextBase64
    }

    companion object {

        fun fromBase64(value:String) : Secret {
            return Secret(Base64.getDecoder().decode(value))
        }

        fun decryptBase64(value: ByteArray, keyFile: SecretKey, handler: CipherHandler): String {
            val decoded = Base64.getDecoder().decode(value)
            return String(decrypt(decoded, keyFile, handler))
        }

        fun encrypt(value: ByteArray, keyFile: SecretKey, handler: CipherHandler) = handler.encrypt(value, keyFile)

        fun decrypt(value: ByteArray, keyFile: SecretKey, handler: CipherHandler) = handler.decrypt(keyFile, value)
    }

}