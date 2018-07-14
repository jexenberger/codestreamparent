package io.codestream.runtime

class SimpleGroupTaskContext {
    var error: Boolean = false
    var before: Boolean = false
    var after: Boolean = false
    var onFinally: Boolean = false
}