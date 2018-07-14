package io.codestream.api

class ModuleDoesNotExistException(val moduleName:String) : CodeStreamRuntimeException(moduleName, "does not exist") {
}