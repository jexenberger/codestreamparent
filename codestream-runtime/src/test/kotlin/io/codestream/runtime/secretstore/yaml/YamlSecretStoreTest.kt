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

package io.codestream.runtime.secretstore.yaml

import io.codestream.util.crypto.Secret
import org.junit.jupiter.api.Test
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.util.*
import kotlin.test.junit.JUnitAsserter.assertNotNull

class YamlSecretStoreTest {


    @Test
    internal fun testCreate() {
        val file = File.createTempFile("yamlsecretstore", UUID.randomUUID().toString())
        file.deleteOnExit()
        val store = io.codestream.runtime.yaml.YamlSecretStore(file)
        store["test"] = Secret("hello world")
        println(Yaml().dump(store.yamlStore))
        val anotherStore = io.codestream.runtime.yaml.YamlSecretStore(file)
        val existing = anotherStore["test"]
        assertNotNull("should have been able to get existing", existing)
    }
}