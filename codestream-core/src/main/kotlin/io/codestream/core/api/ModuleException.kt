package io.codestream.core.api

class ModuleException(val module:CodestreamModule, message:String ) : CodeStreamRuntimeException(module.id.toString(),message)