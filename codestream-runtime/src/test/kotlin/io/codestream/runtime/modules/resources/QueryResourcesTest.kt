/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class QueryResourcesTest {


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
        for (i in 1..5) {
            val oddOrEven = if (i % 2 == 0) "even" else "odd"
            val theResource = Resource(type, i.toString(), mapOf("test" to "test", "oddOrEven" to oddOrEven))
            repo.save(theResource)
        }
        val resource = QueryResources("test::test",parameters = mapOf("oddOrEven" to "odd"))
        val result = resource.evaluate(bindings)
        assertEquals(3, result?.size)
        result?.forEach {
            assertEquals("odd", it["oddOrEven"])
        }
        println(result)
        assertNotNull(result)
    }
}