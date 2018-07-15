package io.codestream.api

interface FunctionalTask : SimpleTask {

    val outputVariable: String get() =  "__output"

    override fun run(ctx: RunContext) {
        val result = getResult(ctx)
        ctx[outputVariable] = result
    }

    fun getResult(ctx: RunContext): Any?

}