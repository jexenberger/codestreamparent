package io.codestream.runtime.tree

import io.codestream.api.events.TaskState

enum class BranchProcessingDirective(val state:TaskState) {

    done(TaskState.completed),
    continueExecution(TaskState.running),
    again(TaskState.running),
    abort(TaskState.aborted)

}