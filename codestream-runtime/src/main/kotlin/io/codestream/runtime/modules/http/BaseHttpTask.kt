package io.codestream.runtime.modules.http

import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext
import io.codestream.api.annotations.Parameter
import io.codestream.util.rest.Request
import io.codestream.util.rest.Response
import java.io.File

abstract class BaseHttpTask : FunctionalTask {


    @Parameter(description = "URI to call")
    var uri: String = ""
    @Parameter(description = "Headers for the HTTP operation", required = false)
    var headers: Map<String, Any?>? = null
    @Parameter(description = "Name of variable to set in context, is set to the file path, it outputFile is specified", required = false, default = "__output")
    override var outputVariable: String = "__output"
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
    @Parameter(description = "Variable to set HTTP response code in context", required = false, default = "__httpStatus")
    var httpStatusVar: String = "__httpStatus"
    @Parameter(description = "Variable to set HTTP response message in context", required = false, default = "__httpResponseMessage")
    var httpResponseMessageVar: String = "__httpResponseMessage"
    @Parameter(description = "Variable to set HTTP response headers in context", required = false, default = "__httpResponseHeadersVar")
    var httpResponseHeadersVar: String = "__httpResponseHeadersVar"


    override fun getResult(ctx: RunContext): Any? {
        val request = Request(uri = uri,
                validateSSL = validateSSL,
                validateHostName = validateHostName)
        headers?.forEach { t, u -> request.header(t, u?.toString() ?: "") }
        proxyHost?.let { request.proxy(it, proxyPort, proxyUser, proxyPassword) }
        outputFile?.let { request.outputFile(it) }
        val (status, responseMessage, body, responseHeaders) = handleRequest(request)
        ctx[httpStatusVar] = status
        ctx[httpResponseMessageVar] = responseMessage
        ctx[httpResponseHeadersVar] = responseHeaders
        return body 
    }

    protected abstract fun handleRequest(request: Request): Response
}