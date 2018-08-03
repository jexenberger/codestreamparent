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

import java.io.File
import java.security.Key
import java.security.KeyStore
import java.security.cert.X509Certificate

class JavaKeyStore(val path: File, val password: String = "changeit") {


    val store: KeyStore = KeyStore.getInstance("JKS")

    init {
        if (path.exists() && path.isFile) {
            store.load(path.inputStream(), password.toCharArray())
        } else {
            store.load(null, password.toCharArray())
            save()
        }
    }

    private fun save() {
        store.store(path.outputStream(), password.toCharArray())
    }

    operator fun get(alias:String): KeyStore.Entry? {
        return store.getEntry(alias, KeyStore.PasswordProtection(password.toCharArray()))
    }

    operator fun set(alias:String, key:Key) {
        store.setKeyEntry(
                alias,
                key,
                password.toCharArray(),
                emptyArray<X509Certificate>()
        )
    }


    fun addKeyAndCertificate(alias: String, key: Key, vararg certificate: X509Certificate) {
        store.setKeyEntry(alias, key, password.toCharArray(), certificate)
        save()
    }


    fun setCertificate(alias: String, certificate: X509Certificate) {
        store.setCertificateEntry(alias, certificate)
        save()
    }

}