package io.codestream.core.runtime.kt

import io.codestream.core.runtime.Component
import io.codestream.core.runtime.ComponentType
import io.codestream.core.runtime.Module
import kotlin.reflect.KClass

abstract class KtModule : Module {

    override val name:String get() = this::class.simpleName!!
    override val version:String get() = "1.0"

    private val components:MutableSet<KClass<KtComponent>> = mutableSetOf()

    abstract fun define()

    override fun createInstance(type: ComponentType): Component {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    operator fun plusAssign(type:KClass<KtComponent>) {
        components += type
    }
}