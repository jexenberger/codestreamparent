package io.codestream.runtime.modules.json

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JsonModuleFunctionsTest {

    @Test
    internal fun testArrayToJson() {
        val json = JsonModuleFunctions().arrayToJson(arrayOf(1, 2, 3))
        assertEquals("[ 1, 2, 3 ]", json)
    }

    @Test
    internal fun testArrayToJsonString() {
        val json = JsonModuleFunctions().arrayToJson(arrayOf("hello", "world"))
        assertEquals("[ \"hello\", \"world\" ]", json)
    }

    @Test
    internal fun testCollectionToJson() {
        val json = JsonModuleFunctions().collectionToJson(listOf(1, 2, 3))
        assertEquals("[ 1, 2, 3 ]", json)
    }
}