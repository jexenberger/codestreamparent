package io.codestream.runtime.task

import io.codestream.api.GroupTask
import io.codestream.api.ModuleId
import io.codestream.api.TaskId
import io.codestream.api.TaskType
import io.codestream.api.metamodel.FunctionalTaskDef
import io.codestream.api.metamodel.ParameterDef
import io.codestream.api.metamodel.TaskDef
import io.codestream.di.api.ScopeType
import io.codestream.di.api.addDependencyHandler
import io.codestream.di.api.addInstance
import io.codestream.di.api.addType
import io.codestream.runtime.SampleFunctionalTask
import io.codestream.runtime.SampleSimpleTask
import io.codestream.runtime.StreamContext
import io.codestream.runtime.TaskDefId
import io.codestream.runtime.container.ParameterDependency
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NonGroupTaskHandlerTest {

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
        val taskDef = TaskDef(taskId, p)
        addDependencyHandler(ParameterDependency())
        val ctx = StreamContext()
        ctx.bindings["value"] = "hello"
        val defn = addType<GroupTask>(SampleSimpleTask::class).toScope(ScopeType.singleton.name) withId taskId
        ctx.add(defn)
        ctx.add(addInstance(taskDef) withId TaskDefId(taskId))
        io.codestream.runtime.task.NonGroupTaskHandler(taskId, taskDef).execute(ctx)
        assertTrue { ctx.get<SampleSimpleTask>(taskId)?.run ?: false }
    }

    @Test
    internal fun testExecuteFunctionalTask() {
        val taskDef = FunctionalTaskDef(taskId, p, { true }, "qwerty")
        addDependencyHandler(ParameterDependency())
        val ctx = StreamContext()
        val defn = addType<GroupTask>(SampleFunctionalTask::class).toScope(ScopeType.singleton.name) withId taskId
        ctx.add(defn)
        ctx.add(addInstance(taskDef) withId TaskDefId(taskId))
        io.codestream.runtime.task.NonGroupTaskHandler(taskId, taskDef).execute(ctx)
        assertEquals("hello world", ctx.bindings["qwerty"])
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
        io.codestream.runtime.task.NonGroupTaskHandler(taskId, taskDef).execute(ctx)
        assertFalse { ctx.get<SampleSimpleTask>(taskId)?.run ?: false }
    }
}