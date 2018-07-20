package io.codestream.api.metamodel

import io.codestream.api.RunContext
import io.codestream.api.TaskId

class FunctionalTaskDef(id: TaskId,
                        parameters: Map<String, ParameterDef>,
                        condition: (RunContext) -> Boolean = { true },
                        val assign:String?) : TaskDef(id, parameters, condition) {
}