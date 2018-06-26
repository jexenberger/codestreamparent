package io.codestream.core.api

interface SimpleTask : Task {

    fun run(ctx:RunContext)

}