package io.codestream.util.crypto

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

interface BaseCipherHandler : CipherHandler {

    override fun generateKey() = generateSecretKey()
    override fun generateSecretKey() = KeyGenerator.getInstance(algorithm).generateKey()

    override fun encrypt(plainText: ByteArray, key: ByteArray): ByteArray {
        val secretKey = SecretKeySpec(key, algorithm)
        return encrypt(plainText, secretKey)
    }

    override fun encrypt(plainText: ByteArray, secretKey: Key): ByteArray {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher.doFinal(plainText)
    }

    override fun decrypt(cipherText: ByteArray, key: ByteArray): ByteArray {
        val secretKey = SecretKeySpec(key, algorithm)
        return decrypt(secretKey, cipherText)
    }

    override fun decrypt(secretKey: Key, cipherText: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return cipher.doFinal(cipherText)
    }
}