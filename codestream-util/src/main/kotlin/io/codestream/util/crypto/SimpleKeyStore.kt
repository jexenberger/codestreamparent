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
import java.util.*

data class SimpleKeyStore(val file: String) {

    val store: Properties = Properties()

    init {
        loadFromFile()
    }

    private fun loadFromFile() {
        val storeFile = File(file)
        if (storeFile.exists() && storeFile.isFile) {
            store.load(storeFile.inputStream())
        }
    }

    @Synchronized
    fun store(name: String, key: ByteArray) {
        store.setProperty(name, String(Base64.getEncoder().encode(key)))
        store.store(File(file).outputStream(), "Written by keystore")
    }

    @Synchronized
    fun load(name: String): ByteArray? {
        return store.getProperty(name)?.let { Base64.getDecoder().decode(it) }
    }

}