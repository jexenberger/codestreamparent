package io.codestream.api

import java.util.*

interface CodestreamFactory {

    fun get(settings: CodestreamSettings): Codestream

    fun get() : Codestream

    fun runContext() : RunContext


    companion object {
        fun get(): CodestreamFactory {
            val serviceLoader = ServiceLoader.load<CodestreamFactory>(io.codestream.api.CodestreamFactory::class.java)
            return serviceLoader.findFirst().orElseThrow { IllegalArgumentException("No default implementation found") }
        }
    }

}