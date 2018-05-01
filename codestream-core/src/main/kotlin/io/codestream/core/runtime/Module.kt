package io.codestream.core.runtime

interface Module {

    val name:String
    val version:String

    fun createInstance(type:ComponentType) : Component

}