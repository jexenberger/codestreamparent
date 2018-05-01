package io.codestream.di.api

interface Factory<T> {

    fun postBinding() :Boolean = false

    fun get(ctx: Context) : T
}