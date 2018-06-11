package io.codestream.di.event

interface EventHandler<T> {

    fun onEvent(event: T)

}