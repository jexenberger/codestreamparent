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