package io.codestream.core.runtime.task

import io.codestream.core.SampleSimpleTask
import io.codestream.core.api.GroupTask
import io.codestream.core.api.TaskType
import io.codestream.core.metamodel.ParameterDef
import io.codestream.core.metamodel.StreamDef
import io.codestream.core.metamodel.TaskDef
import io.codestream.core.runtime.*
import io.codestream.di.api.ScopeType
import io.codestream.di.api.addDependencyHandler
import io.codestream.di.api.addInstance
import io.codestream.di.api.addType
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SimpleTaskHandlerTest {

    private val taskId = TaskId(TaskType(ModuleId("test"), "test"))

    @Test
    internal fun testExecute() {
        val taskDef = TaskDef(taskId,
                mapOf("value" to ParameterDef("value", "\${value}"))
        )
        addDependencyHandler(ParameterDependency())
        val ctx = StreamContext()
        ctx.bindings["value"] = "hello"
        val defn = addType<GroupTask>(SampleSimpleTask::class).toScope(ScopeType.singleton.name) withId taskId
        ctx.add(defn)
        ctx.add(addInstance(taskDef) withId TaskDefId(taskId))
        SimpleTaskHandler(taskId, taskDef).execute(ctx)
        assertTrue { ctx.get<SampleSimpleTask>(taskId)?.run ?: false }
    }

    @Test
    internal fun testExecuteConditionally() {
        val taskDef = TaskDef(
                taskId,
                mapOf("value" to ParameterDef("value", "\${value}")),
                { false }
        )
        addDependencyHandler(ParameterDependency())
        val ctx = StreamContext()
        ctx.bindings["value"] = "hello"
        val defn = addType<GroupTask>(SampleSimpleTask::class).toScope(ScopeType.singleton.name) withId taskId
        ctx.add(defn)
        ctx.add(addInstance(taskDef) withId TaskDefId(taskId))
        SimpleTaskHandler(taskId, taskDef).execute(ctx)
        assertFalse { ctx.get<SampleSimpleTask>(taskId)?.run ?: false }
    }
}