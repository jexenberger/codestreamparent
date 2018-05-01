package io.codestream.di.api

import java.util.*
import javax.script.Bindings
import kotlin.reflect.KClass


interface Context {

    val values: Map<String, Any>

    operator fun <T> get(id: ComponentId): T?

    fun <T> eval(expr: String, bindings: Bindings = DependencyInjectionBindings(this, LinkedHashMap(values))): T?
    fun <T> value(key: String): T?

    fun <T> get(name: String) = get(StringId(name)) as T?
    fun <T> get(type: KClass<*>) = get(TypeId(type)) as T?

}
