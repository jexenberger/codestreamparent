package io.codestream.api

interface FunctionalTask : SimpleTask {

    val outputVariable: String

    override fun run(ctx: RunContext) {
        val result = getResult(ctx)
        ctx[outputVariable] = result
    }

    fun getResult(ctx: RunContext): Any?

}