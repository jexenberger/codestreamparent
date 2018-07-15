package io.codestream.runtime.modules.http

import io.codestream.api.annotations.Task
import io.codestream.util.rest.Request
import io.codestream.util.rest.Response

@Task(name = "put", description = "Performs an HTTP Put")
class Put : BaseBodiedHttpTask() {
    override fun handleRequest(request: Request): Response {
        if (body == null && attachments == null) {
            throw IllegalStateException("Both body and attachments are empty")
        }
        request.body(body!!)
        return request.put()
    }

}