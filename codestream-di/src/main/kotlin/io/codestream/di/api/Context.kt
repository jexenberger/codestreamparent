package io.codestream.di.api

import io.codestream.di.event.EventDispatcher
import java.util.*
import javax.script.Bindings
import kotlin.reflect.KClass


interface Context {

    val values: Map<String, Any>

    val events: EventDispatcher

    val bindings:Bindings

    operator fun <T> get(id: ComponentId): T?


    fun <T> value(key: String): T?

    fun <T> get(name: String) = get(StringId(name)) as T?
    fun <T> get(type: KClass<*>) = get(TypeId(type)) as T?



}
