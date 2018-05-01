package io.codestream.di.api

import java.util.*

abstract class BaseModule(val name: String) : DIModule {

    private val _ctx: DefinableContext by lazy {
        val factory = ServiceLoader.load(DefinableContextFactory::class.java)
                .findFirst()
                .orElseThrow { IllegalStateException("no context factory defined") }
        val ctx = factory.ctx()
        define(ctx)
        ctx
    }

    override fun name() = this.name


    override fun ctx(): Context = _ctx

    abstract fun define(ctx: DefinableContext);

}