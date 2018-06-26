package io.codestream.core.api

class TaskDoesNotExistException(val type:TaskType) : CodeStreamRuntimeException(type.toString(), "does not exist") {
}