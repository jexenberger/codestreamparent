package io.codestream.runtime.modules.http

import io.codestream.api.annotations.Parameter
import java.io.File

abstract class BaseBodiedHttpTask : BaseHttpTask() {

    @Parameter(description = "Body to submit with request", required = false)
    var body: String? = null

    @Parameter(description = "List of files to upload with the request", required = false)
    val attachments: Array<File>? = emptyArray()

}