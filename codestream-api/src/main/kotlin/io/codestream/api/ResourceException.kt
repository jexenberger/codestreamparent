package io.codestream.api

class ResourceException(componentId: String, msg: String) : CodeStreamRuntimeException(componentId, msg) {
}