package io.codestream.runtime

import io.codestream.api.Codestream
import io.codestream.api.CodestreamFactory
import io.codestream.api.CodestreamSettings
import io.codestream.api.RunContext

class CodestreamRuntimeFactory: CodestreamFactory {

    override fun get(settings: CodestreamSettings): Codestream {
        return CodestreamRuntime(settings)
    }

    override fun get() : Codestream {
        return get(CodestreamSettings())
    }

    override fun runContext() : RunContext {
        return StreamContext().bindings
    }
}