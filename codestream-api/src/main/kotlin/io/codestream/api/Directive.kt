package io.codestream.api

import io.codestream.api.events.TaskState

enum class Directive(val state:TaskState) {

    done(TaskState.completed),
    continueExecution(TaskState.running),
    again(TaskState.running),
    abort(TaskState.aborted)

}