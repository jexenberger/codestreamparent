package io.codestream.di.api

import kotlin.reflect.KClass

data class TypeId(val type: KClass<*>) : ComponentId {

    constructor(type: Any) : this(type::class)

    override val stringId: String
        get() = type.qualifiedName!!

}