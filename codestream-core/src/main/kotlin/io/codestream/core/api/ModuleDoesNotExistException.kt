package io.codestream.core.api

class ModuleDoesNotExistException(val moduleName:String) : CodeStreamRuntimeException(moduleName, "does not exist") {
}