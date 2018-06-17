package io.codestream.core.runtime.task

import io.codestream.core.api.TaskContext
import io.codestream.util.Eval
import javax.script.Bindings

val defaultCondition = { ctx: TaskContext -> true }
fun scriptCondition(script: String) = { ctx: TaskContext -> Eval.eval<Boolean>(script, (ctx as Bindings)) }