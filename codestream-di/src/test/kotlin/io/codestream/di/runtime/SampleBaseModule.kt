package io.codestream.di.runtime

import io.codestream.di.api.BaseModule
import io.codestream.di.api.DefinableContext
import io.codestream.di.api.addType
import io.codestream.di.sample.AnotherObject
import io.codestream.di.sample.SampleObject

class SampleBaseModule() : BaseModule("sample") {
    override fun define(ctx: DefinableContext) {
        addType<AnotherObject>(AnotherObject::class) into ctx
        addType<SampleObject>(SampleObject::class) into ctx
    }
}