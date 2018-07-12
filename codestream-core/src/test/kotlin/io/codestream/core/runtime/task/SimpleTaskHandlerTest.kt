package io.codestream.core.runtime.task

import io.codestream.core.runtime.SampleSimpleTask
import io.codestream.core.api.GroupTask
import io.codestream.core.api.ModuleId
import io.codestream.core.api.TaskId
import io.codestream.core.api.TaskType
import io.codestream.core.api.metamodel.ParameterDef
import io.codestream.core.api.metamodel.TaskDef
import io.codestream.core.runtime.*
import io.codestream.core.runtime.container.ParameterDependency
import io.codestream.di.api.ScopeType
import io.codestream.di.api.addDependencyHandler
import io.codestream.di.api.addInstance
import io.codestream.di.api.addType
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SimpleTaskHandlerTest {

    private val taskId = TaskId(TaskType(ModuleId.fromString("test"), "test"))

    private val p = mapOf(
            "value" to ParameterDef("value", "\${value}"),
            "simple" to ParameterDef("value", "\${value}"),
            "arrayString" to ParameterDef("value", "hello, world"),
            "arrayInt" to ParameterDef("value", "1,2,3"),
            "map" to ParameterDef("value", mapOf("test" to "test"))
    )

    @Test
    internal fun testExecute() {
        val taskDef = TaskDef(taskId,p)
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
                p,
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