package io.codestream.util.crypto

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SystemKeyTest {

    @Test
    internal fun testCreate() {
        val key = SystemKey()
        assertTrue { key.file.exists() }
        key.file.delete()
        val newKey = SystemKey()
        assertTrue { newKey.file.exists() }
        assertNotNull(newKey.key)
    }
}