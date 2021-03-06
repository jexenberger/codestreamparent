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

package io.codestream.runtime.modules.http

import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.util.rest.Request
import io.codestream.util.rest.Response
import java.io.File

abstract class BaseHttpTask : FunctionalTask<Map<String, Any?>> {


    @Parameter(description = "URI to call")
    var uri: String = ""
    @Parameter(description = "Headers for the HTTP operation", required = false)
    var headers: Map<String, Any?>? = null
    @Parameter(description = "Path of an output file to dump result to", required = false)
    var outputFile: File? = null
    @Parameter(description = "Validate SSL connection", required = false, default = "true")
    var validateSSL: Boolean = true
    @Parameter(description = "Validate Hostname on SSL connection", required = false, default = "true")
    var validateHostName: Boolean = true
    @Parameter(description = "Proxy server to use for the request (can be set in system or globally in ~/.cs/proxy.settings)", required = false)
    var proxyHost: String? = null
    @Parameter(description = "Proxy user to use for the request (can be set in system or globally in ~/.cs/proxy.settings)", required = false)
    var proxyUser: String? = null
    @Parameter(description = "Proxy password to use for the request (can be set in system or globally in ~/.cs/proxy.settings)", required = false)
    var proxyPassword: String? = null
    @Parameter(description = "Proxy port to use for the request (can be set in system or globally in ~/.cs/proxy.settings)", required = false, default = "8080")
    var proxyPort: Int = 8080


    override fun evaluate(ctx: RunContext): Map<String, Any?>? {
        val request = Request(uri = uri,
                validateSSL = validateSSL,
                validateHostName = validateHostName)
        headers?.forEach { t, u -> request.header(t, u?.toString() ?: "") }
        proxyHost?.let { request.proxy(it, proxyPort, proxyUser, proxyPassword) }
        outputFile?.let { request.outputFile(it) }
        val (status, responseMessage, body, responseHeaders) = handleRequest(request)
        val httpReponse = mapOf(
                "status" to status,
                "message" to responseMessage,
                "headers" to responseHeaders,
                "body" to body
        )
        return httpReponse
    }

    protected abstract fun handleRequest(request: Request): Response
}