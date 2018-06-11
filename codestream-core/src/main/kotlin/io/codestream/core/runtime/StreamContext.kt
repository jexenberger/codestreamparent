package io.codestream.core.runtime

import io.codestream.di.api.ApplicationContext

class StreamContext() : ApplicationContext(mutableMapOf(), mutableMapOf()) {

    private var _bindings : ScopedDependencyBindings = ScopedDependencyBindings(this)

    override val bindings: ScopedDependencyBindings get() = _bindings

    constructor(theBindings: ScopedDependencyBindings) : this() {
        _bindings = theBindings
    }


    fun subContext() = StreamContext(this.bindings.subBindings())
}