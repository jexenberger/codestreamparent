package io.codestream.util.crypto

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class DESedeTest {


    @Test
    fun testEncrypt() {
        val key = DESede.generateKey()
        val aes = DESede()
        val encrypt = aes.encrypt("hello world".toByteArray(), key.encoded)
        assertNotNull(encrypt)
        val decrypt = aes.decrypt(encrypt, key.encoded)
        assertEquals("hello world", String(decrypt))

    }
}