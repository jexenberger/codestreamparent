package io.codestream.core.api

class ModuleException(val module:Module,  message:String ) : CodeStreamRuntimeException(module.id.toString(),message) {
}