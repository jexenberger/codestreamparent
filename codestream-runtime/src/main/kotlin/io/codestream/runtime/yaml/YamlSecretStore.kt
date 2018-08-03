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

package io.codestream.runtime.yaml

import io.codestream.util.crypto.Secret
import java.io.File
import io.codestream.util.crypto.SimpleSecretStore
import org.yaml.snakeyaml.Yaml

class YamlSecretStore(val file:File) : SimpleSecretStore {

    internal val yamlStore:MutableMap<String, String>

    init {
        val store = if (file.exists()) Yaml().load(file.reader()) as MutableMap<String, String>? else mutableMapOf()
        yamlStore = store?.let { it } ?: mutableMapOf()
        save()
    }

    override fun get(keyName: String): Secret? {
        return yamlStore[keyName]?.let { Secret.fromBase64(it) }
    }

    @Synchronized
    override fun set(keyName: String, secret: Secret) {
        yamlStore[keyName] = secret.cipherTextBase64
        save()
    }

    private fun save() {
        Yaml().dump(yamlStore, file.writer())
    }
}