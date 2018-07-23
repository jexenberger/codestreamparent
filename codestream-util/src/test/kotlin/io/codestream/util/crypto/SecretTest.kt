package io.codestream.util.crypto

import io.codestream.util.transformation.TransformerService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class SecretTest {


    private val key = SystemKey.get()
    private val handler = AES()


    @Test
    fun testDecrypt() {

        val encrypt = handler.encrypt("hello world".toByteArray(), key.encoded)
        val decrypt = Secret.decrypt(encrypt, key, handler)
        assertEquals("hello world", String(decrypt))

    }

    @Test
    fun testEncrypt() {
        val encrypt = handler.encrypt("hello world".toByteArray(), key.encoded)
        val check = Secret.encrypt("hello world".toByteArray(), key, handler)
        assertEquals(String(encrypt), String(check))
    }

    @Test
    fun testCipherTextBase64() {
        val encrypt = Base64.getEncoder().encodeToString(handler.encrypt("hello world".toByteArray(), key.encoded))
        val secret = Secret("hello world", key)
        assertEquals(encrypt, secret.cipherTextBase64)
    }

    @Test
    fun testToAndFromString() {
        val secret = TransformerService.convert<Secret>("hello world")
        val plainText = TransformerService.convert<String>(secret)
        assertEquals("hello world", plainText)
    }
}