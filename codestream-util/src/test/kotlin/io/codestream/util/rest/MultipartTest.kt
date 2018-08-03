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

package io.codestream.util.rest

import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.test.assertEquals

class MultipartTest {

    @Test
    fun testWrite() {
        val multipart = Multipart(File("src/test/resources/nested.yml"), "application/x-yaml")
        val bos = ByteArrayOutputStream()
        multipart.write(bos, UUID.randomUUID().toString())
        println(bos.toString())
    }

    @Test
    fun testWriteMany() {
        val part = Multipart(File("src/test/resources/nested.yml"), "application/x-yaml")
        val byteArrayOutputStream = ByteArrayOutputStream()
        Multipart.write(byteArrayOutputStream, "UTF-8", Multipart.getId(), arrayOf(part))
        println(byteArrayOutputStream.toString())
    }

    @Test
    fun testGetFileType() {
        assertEquals("application/x-yaml", Multipart.contentTypeFromFile(File("src/test/resources/test_layout.yaml")))
        assertEquals("application/x-yaml", Multipart.contentTypeFromFile(File("src/test/resources/test_layout.yml")))
        assertEquals("application/pdf", Multipart.contentTypeFromFile(File("src/test/resources/test_layout.pdf")))
        assertEquals("application/xml", Multipart.contentTypeFromFile(File("src/test/resources/test_layout.xml")))
        assertEquals("application/octet-stream", Multipart.contentTypeFromFile(File("src/test/resources/test_layout.qweqwe")))
    }
}