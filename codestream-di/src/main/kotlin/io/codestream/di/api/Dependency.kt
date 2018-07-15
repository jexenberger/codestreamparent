package io.codestream.di.api

import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter

interface Dependency {

    fun supports(type: KParameter): Boolean

    fun supports(type: KMutableProperty<*>): Boolean

    fun <T> resolve(target: DependencyTarget, ctx: Context): T?

}