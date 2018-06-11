package io.codestream.core.runtime

import java.util.*

object ModuleRegistry {

    fun module(name:String) : Module? {
        val serviceLoader = ServiceLoader.load<Module>(Module::class.java)
        return serviceLoader.find { it.name == name }
    }

    operator fun get(name: String) = module(name)
}