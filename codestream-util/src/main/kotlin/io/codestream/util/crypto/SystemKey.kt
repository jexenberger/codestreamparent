package io.codestream.util.crypto

import io.codestream.util.OS
import java.io.File
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.SecretKey
import javax.crypto.spec.PBEParameterSpec


class SystemKey(val file: File = File("${OS.os().homeDir}/.systemkey.p12"), val password: String = "changeit", val handler:CipherHandler = AES()) {

    private val pkcs12KeyStore = KeyStore.getInstance("PKCS12")


    val key get() = loadKey()

    init {
        if (!file.exists()) {
            pkcs12KeyStore.load(null, null)
            generateKey()
            save()
        } else {
            pkcs12KeyStore.load(file.inputStream(), password.toCharArray())
        }
    }


    fun save() {
        pkcs12KeyStore.store(file.outputStream(), password.toCharArray())
    }

    fun generateKey() {
        val key = handler.generateSecretKey()
        val salt = ByteArray(20)
        SecureRandom().nextBytes(salt)
        pkcs12KeyStore.setEntry("key", KeyStore.SecretKeyEntry(key),
                KeyStore.PasswordProtection(
                        password.toCharArray(),
                        "PBEWithHmacSHA512AndAES_128",
                        PBEParameterSpec(salt, 100_000)
                )

        )
        pkcs12KeyStore.store(file.outputStream(), password.toCharArray())
    }

    fun loadKey(): SecretKey {
        return pkcs12KeyStore.getKey("key", password.toCharArray()) as SecretKey
    }

    companion object {
        var systemKeyPath = File("${OS.os().homeDir}/.systemkey.p12")

        fun get(file: File = systemKeyPath) : SecretKey {
            return SystemKey(file).key
        }
    }


}