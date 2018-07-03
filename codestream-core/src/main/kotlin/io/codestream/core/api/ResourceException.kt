package io.codestream.core.api

class ResourceException(componentId: String, msg: String) : CodeStreamRuntimeException(componentId, msg) {
}