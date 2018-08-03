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
import java.security.KeyStore
import javax.net.ssl.*


object SSL {


    fun defaultStoreAndKey(host:String, path:String) : JavaKeyStore {
        val (key, certificate) = SSLCertificate(
                cn=host,
                ou="org-unit",
                org="organisation",
                locality ="na",
                state = "na"
        ).selfSignedCertificate
        val store = JavaKeyStore(File(path))
        store.addKeyAndCertificate(host, key, certificate)
        return store
    }

    fun ctx(keyStore: KeyStore, trustStore: KeyStore, password: String = "changeit"): SSLContext {
        val keyManagers: Array<KeyManager>
        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyManagerFactory.init(keyStore, password.toCharArray())
        keyManagers = keyManagerFactory.getKeyManagers()

        val trustManagers: Array<TrustManager>
        val trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(trustStore)
        trustManagers = trustManagerFactory.getTrustManagers()

        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(keyManagers, trustManagers, null)
        return sslContext
    }
}