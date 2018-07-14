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