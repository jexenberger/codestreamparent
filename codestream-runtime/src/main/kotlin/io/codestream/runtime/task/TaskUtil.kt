package io.codestream.runtime.task

import io.codestream.api.RunContext
import io.codestream.util.script.Eval
import javax.script.Bindings

val defaultCondition = { ctx: RunContext -> true }
fun scriptCondition(script: String) = { ctx: RunContext -> Eval.eval<Boolean>(script, (ctx as Bindings)) }