package io.codestream.api

interface SimpleTask : Task {

    fun run(ctx: RunContext)

}