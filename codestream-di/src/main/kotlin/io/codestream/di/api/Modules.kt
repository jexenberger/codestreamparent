package io.codestream.di.api

import java.util.*

val DEFAULT_MODULE = "__default__"

private val modules:Modules = Modules()

@Synchronized
fun module(builder: ApplicationContext.() -> Unit): Context {
    val ctx = modules[DEFAULT_MODULE]!!.ctx() as ApplicationContext
    ctx.builder()
    return ctx
}

class Modules {


    private val modules: Map<String, DIModule>

    init {
        val serviceLoader = ServiceLoader.load(DIModule::class.java)
        val DIModulePairs: MutableList<Pair<String, DIModule>> = mutableListOf()
        for (service in serviceLoader) {
            DIModulePairs += service.name() to service
        }
        this.modules = DIModulePairs.toMap()
    }

    operator fun get(name: String): DIModule? {
        return modules[name]
    }


}