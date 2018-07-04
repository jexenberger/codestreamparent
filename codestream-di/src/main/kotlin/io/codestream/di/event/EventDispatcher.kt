package io.codestream.di.event

import io.codestream.util.ifTrue
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

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
        handlers.forEach { (k, handler) ->
            val run = k.isInstance(event) || k.isSubclassOf(event::class)
            run.ifTrue {
                handler.forEach { (it as EventHandler<T>).onEvent(event) }
            }
        }
    }

}