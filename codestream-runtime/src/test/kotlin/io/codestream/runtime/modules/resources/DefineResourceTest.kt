package io.codestream.runtime.modules.resources

import io.codestream.api.resources.ResourceType
import io.codestream.runtime.StreamContext
import io.codestream.runtime.resources.yaml.YamlResourceRepository
import io.codestream.util.system
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertNotNull

class DefineResourceTest {


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
        val bindings = StreamContext().bindings
        bindings["_resources"] = repo
        val resource = DefineResource("test","test::test", mapOf("test" to "test"))
        resource.run(bindings)
        val lookup = repo.get(ResourceType.fromString("test::test"), "test")
        assertNotNull(lookup)
    }
}