package io.codestream.api

class ModuleException(val module: CodestreamModule, message:String ) : CodeStreamRuntimeException(module.id.toString(),message)