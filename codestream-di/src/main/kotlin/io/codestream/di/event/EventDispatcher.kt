package io.codestream.di.event

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

class EventDispatcher {

    private val handlers: MutableMap<KClass<*>, MutableList<EventHandler<*>>> = mutableMapOf()

    @Synchronized
    fun <T> register(handler: EventHandler<T>) {
        val type = ((handler::class.java.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0] as Class<*>)
        val kType = type.kotlin
        val handlersForType = handlers[kType] ?: run {
            val list = mutableListOf<EventHandler<*>>()
            handlers[kType] = list
            list
        }
        handlersForType += handler
    }

    fun <T : Any> publish(event: T) {
        handlers[event::class]?.let { handler -> handler.forEach { (it as EventHandler<T>).onEvent(event) } }
    }

}