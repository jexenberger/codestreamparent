package io.codestream.runtime.services

import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class MvelTemplateServiceTest {

    @Test
    internal fun testWrite() {
        val service = MvelTemplateService()
        val target = ByteArrayOutputStream()
        service.write(ByteArrayInputStream("hello @{value}".toByteArray()), target, mapOf("value" to "John"))
        assertEquals("hello John", String(target.toByteArray()))
    }
}