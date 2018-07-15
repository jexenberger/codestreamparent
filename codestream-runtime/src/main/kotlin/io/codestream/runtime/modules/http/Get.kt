package io.codestream.runtime.modules.http

import io.codestream.api.annotations.Task
import io.codestream.util.rest.Request

@Task(name = "get", description = "Performs an HTTP Get")
class Get : BaseHttpTask() {
    override fun handleRequest(request: Request) = request.get()
}