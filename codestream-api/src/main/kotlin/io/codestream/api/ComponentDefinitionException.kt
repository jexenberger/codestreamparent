package io.codestream.api

class ComponentDefinitionException(componentId: String, msg: String) : CodeStreamRuntimeException(componentId, msg)