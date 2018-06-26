package io.codestream.core.runtime.task

import io.codestream.core.api.RunContext
import io.codestream.util.Eval
import javax.script.Bindings

val defaultCondition = { ctx: RunContext -> true }
fun scriptCondition(script: String) = { ctx: RunContext -> Eval.eval<Boolean>(script, (ctx as Bindings)) }