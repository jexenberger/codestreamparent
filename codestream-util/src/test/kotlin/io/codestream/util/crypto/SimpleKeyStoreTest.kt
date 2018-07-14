package io.codestream.util.crypto

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class SimpleKeyStoreTest {

    @Test
    fun testStore() {
        val crypto = DESede()
        val result = crypto.encrypt("hello world".toByteArray(), DESede.generateKey().encoded)
        val file = createTempFile("crypto", "test").absolutePath
        val ks = SimpleKeyStore(file)
        ks.store("test-key", result)
        val props = Properties()
        props.load(File(file).reader())
        assertTrue { props.containsKey("test-key") }
    }

    @Test
    fun testLoad() {
        val crypto = DESede()
        val result = crypto.encrypt("hello world".toByteArray(), DESede.generateKey())
        val file = createTempFile("crypto", "test").absolutePath
        val ks = SimpleKeyStore(file)
        ks.store("test-key", result)
        ks.load("test-key")
        assertNotNull(ks.load("test-key"))
    }


}