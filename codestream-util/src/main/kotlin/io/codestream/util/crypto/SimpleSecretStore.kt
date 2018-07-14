package io.codestream.util.crypto

interface SimpleSecretStore {


    operator fun get(keyName: String): Secret?

    operator fun set(keyName: String, secret: Secret)

}