package io.codestream.util.crypto

class ResolvingSecretStore(var primary:SimpleSecretStore) : SimpleSecretStore {

    val stores:MutableSet<SimpleSecretStore> = mutableSetOf(primary)

    override fun get(keyName: String): Secret? {
        for (store in stores) {
            val result = store[keyName]
            if (result != null) {
                return result
            }
        }
        return null
    }

    override fun set(keyName: String, secret: Secret) {
        primary[keyName] = secret
    }



}