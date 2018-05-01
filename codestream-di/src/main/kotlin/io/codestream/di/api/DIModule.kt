package io.codestream.di.api

interface DIModule {

    fun name(): String
    fun ctx(): Context
    fun <T> ctx(id:ComponentId) : T? = ctx().get(id) as T?
}