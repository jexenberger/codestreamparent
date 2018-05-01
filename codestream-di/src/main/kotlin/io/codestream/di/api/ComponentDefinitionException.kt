package io.codestream.di.api

import io.codestream.di.api.ComponentId

class ComponentDefinitionException(name: ComponentId, msg: String) : RuntimeException("[${name.stringId}] -> $msg") {

}