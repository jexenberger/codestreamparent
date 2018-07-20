package io.codestream.api

interface FunctionalTask<T> : Task {

    fun evaluate(ctx: RunContext): T?

}