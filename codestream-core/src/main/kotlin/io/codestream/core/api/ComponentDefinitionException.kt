package io.codestream.core.api

class ComponentDefinitionException(componentId: String, msg: String) : CodeStreamRuntimeException(componentId, msg)