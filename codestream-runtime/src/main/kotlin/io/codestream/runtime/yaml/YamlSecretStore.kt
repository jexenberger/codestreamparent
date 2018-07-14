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