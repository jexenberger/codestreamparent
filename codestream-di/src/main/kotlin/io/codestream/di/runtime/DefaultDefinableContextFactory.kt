package io.codestream.di.runtime

import io.codestream.di.api.DefinableContext
import io.codestream.di.api.DefinableContextFactory

class DefaultDefinableContextFactory : DefinableContextFactory {
    override fun ctx(): DefinableContext = DefaultApplicationContext()
}