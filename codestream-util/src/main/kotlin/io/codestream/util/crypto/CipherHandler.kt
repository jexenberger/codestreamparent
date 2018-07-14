package io.codestream.util.crypto

import java.security.Key
import javax.crypto.SecretKey

interface CipherHandler {

    val algorithm: String

    fun generateKey(): Key
    fun generateSecretKey(): SecretKey

    fun encrypt(plainText: ByteArray, key: ByteArray): ByteArray
    fun encrypt(plainText: ByteArray, secretKey: Key): ByteArray

    fun decrypt(cipherText: ByteArray, key: ByteArray): ByteArray
    fun decrypt(secretKey: Key, cipherText: ByteArray): ByteArray
}