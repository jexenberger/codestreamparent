package io.codestream.runtime.resources.yaml

import io.codestream.api.Type
import io.codestream.api.descriptor.ParameterDescriptor
import io.codestream.api.resources.Resource
import io.codestream.api.resources.ResourceTemplate
import io.codestream.api.resources.ResourceType
import io.codestream.api.resources.ResourceTypeRegistry
import io.codestream.util.system
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File;
import kotlin.test.assertEquals

class YamlResourceRepositoryTest {


    var dbPath: File

    private val repo: YamlResourceRepository

    private val resourceType: ResourceType = ResourceType("category", "name")

    init {
        dbPath = File("${system.homeDir}/.cstest")
        val properties = mapOf(
                "a" to ParameterDescriptor("a", "test a", Type.string),
                "b" to ParameterDescriptor("b", "test b", Type.intArray),
                "c" to ParameterDescriptor("c", "test c", Type.keyValue),
                "d" to ParameterDescriptor("d", "test d", Type.string, required = false)
        )
        val type = resourceType
        val template = ResourceTemplate(type, "a test template", properties)
        ResourceTypeRegistry += template
        repo =  YamlResourceRepository("test", dbPath)
    }

    @BeforeEach
    internal fun setUp() {
        dbPath.mkdirs()
    }

    @AfterEach
    internal fun tearDown() {
        dbPath.deleteRecursively()
    }

    @Test
    internal fun testSave() {

        val resource = Resource(resourceType, "test123", mapOf(
                "a" to "hello world",
                "b" to arrayOf(1, 2, 3),
                "c" to mapOf<String, Any>("hello" to "world"))
        )
        repo.save(resource)
        val loadedResource = repo.get(resourceType, "test123")!!
        assertEquals(resource, loadedResource)
    }


    @Test
    internal fun testFind() {
        for (i in 0..9) {
            val resource = Resource(resourceType, "test$i", mapOf(
                    "a" to "hello world",
                    "b" to arrayOf(1, 2, 3),
                    "c" to mapOf<String, Any>("hello" to "world"))
            )
            repo.save(resource)
        }
        val result = repo.find(resourceType)
        assertEquals(10, result.size)
        result.forEachIndexed { idx,it->
            assertEquals("test$idx", it.id)
        }
    }

    @Test
    internal fun testFindWithAttributes() {
        for (i in 0..9) {
            val aVal = if (i % 2 == 0)  "even" else "odd"
            val resource = Resource(resourceType, "test$i", mapOf(
                    "a" to aVal,
                    "b" to arrayOf(1, 2, 3),
                    "c" to mapOf<String, Any>("hello" to "world"))
            )
            repo.save(resource)
        }
        val result = repo.find(resourceType, mapOf("a" to "odd"))
        assertEquals(5, result.size)
        result.forEachIndexed { idx,it->
            assertEquals("odd", it["a"])
        }
    }
}