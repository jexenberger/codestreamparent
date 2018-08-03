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

import java.io.File
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*


data class Multipart(val file: File, val contentType: String = contentTypeFromFile(file), val encoding: String = "UTF-8") {


    fun write(stream: OutputStream, id: String) {
        val httpRequestBodyWriter = stream.bufferedWriter(Charset.forName(encoding))

        httpRequestBodyWriter.write("\n$id\n")
        httpRequestBodyWriter.write("Content-Disposition: form-data;"
                + "name=\"${file.nameWithoutExtension}\";"
                + "filename=\"" + file.name + "\""
                + "\nContent-Type: $contentType\n")
        httpRequestBodyWriter.write("Content-Transfer-Encoding: binary\n\n")
        httpRequestBodyWriter.flush()
        file.inputStream().copyTo(stream)
        httpRequestBodyWriter.flush()

    }

    companion object {

        private val contentTypes = mapOf(
                "yaml" to "application/x-yaml",
                "yml" to "application/x-yaml",
                "json" to "application/json",
                "html" to "text/html",
                "htm" to "text/html",
                "js" to "application/javascript",
                "pdf" to "application/pdf",
                "xml" to "application/xml",
                "zip" to "application/zip",
                "txt" to "text/plain"
        )

        fun contentTypeFromFile(file: File) = contentTypes[file.extension] ?: "application/octet-stream"

        fun getId() = "------------------------${UUID.randomUUID()}"

        fun write(stream: OutputStream, encoding: String, id: String = getId(), parts: Array<Multipart>) {
            for (part in parts) {
                part.write(stream, id)
            }
            val httpRequestBodyWriter = stream.bufferedWriter(Charset.forName(encoding))
            httpRequestBodyWriter.write("\n$id--\n")
            httpRequestBodyWriter.flush()
        }
    }
}