package io.codestream.api

import de.skuzzle.semantic.Version
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModuleIdTest {

    @Test
    internal fun testFromString() {
        val id = ModuleId.fromString("hello")
        assertEquals("hello", id.name)
        assertEquals(defaultVersion, id.version)
    }

    @Test
    internal fun testFromStringWithVersion() {
        val id = ModuleId.fromString("hello@1.0.0")
        assertEquals("hello", id.name)
        assertEquals(Version.create(1, 0, 0), id.version)
    }
}