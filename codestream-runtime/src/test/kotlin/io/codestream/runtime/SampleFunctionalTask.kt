package io.codestream.runtime

import io.codestream.api.FunctionalTask
import io.codestream.api.RunContext

class SampleFunctionalTask : FunctionalTask<String> {
    override fun evaluate(ctx: RunContext): String? {
        return "hello world"
    }
}