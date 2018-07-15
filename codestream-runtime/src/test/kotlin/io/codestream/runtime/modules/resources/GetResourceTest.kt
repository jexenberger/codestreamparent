package io.codestream.runtime.modules.resources

import io.codestream.api.resources.Resource
import io.codestream.api.resources.ResourceType
import io.codestream.runtime.StreamContext
import io.codestream.runtime.resources.yaml.YamlResourceRepository
import io.codestream.util.system
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertNotNull

class GetResourceTest {


    var dbPath: File

    private val repo: YamlResourceRepository


    init {
        dbPath = File("${system.homeDir}/.cstest")
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
    internal fun testRun() {
        val type = ResourceType.fromString("test::test")
        val bindings = StreamContext().bindings
        bindings["_resources"] = repo
        val theResource = Resource(type, "test", mapOf("test" to "test"))
        repo.save(theResource)
        val resource = GetResource("test","test::test")
        resource.run(bindings)
        val result = bindings[resource.outputVariable]
        assertNotNull(result)
    }
}