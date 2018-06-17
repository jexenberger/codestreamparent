package io.codestream.core.runtime.task

import io.codestream.core.SimpleGroupTask
import io.codestream.core.api.GroupTask
import io.codestream.core.api.TaskType
import io.codestream.core.metamodel.GroupTaskDef
import io.codestream.core.metamodel.StreamDef
import io.codestream.core.runtime.ModuleId
import io.codestream.core.runtime.StreamContext
import io.codestream.core.runtime.TaskId
import io.codestream.core.runtime.Type
import io.codestream.di.api.ScopeType
import io.codestream.di.api.addType
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GroupTaskHandlerTest {

    private val taskId = TaskId(TaskType(ModuleId("test"), "test"))

    private val defaultTaskDef = GroupTaskDef(
            taskId,
            emptyMap(),
            false
    )

    @Test
    internal fun testPreTraversal() {
        val taskDef = defaultTaskDef
        val ctx = StreamContext()
        val defn = addType<GroupTask>(SimpleGroupTask::class).toScope(ScopeType.singleton.name) withId taskId
        ctx.add(defn)
        val handler = GroupTaskHandler(taskId, false, taskDef)
        handler.preTraversal(ctx)
        assertTrue { ctx.get<SimpleGroupTask>(taskId)?.before ?: false }
    }

    @Test
    internal fun testPreTraversalConditional() {
        val taskDef = GroupTaskDef(
                taskId,
                emptyMap(),
                false,
                { false }
        )
        val ctx = StreamContext()
        val defn = addType<GroupTask>(SimpleGroupTask::class).toScope(ScopeType.singleton.name) withId taskId
        ctx.add(defn)
        val handler = GroupTaskHandler(taskId, false, taskDef)
        handler.preTraversal(ctx)
        assertFalse { ctx.get<SimpleGroupTask>(taskId)?.before ?: true }
    }

    @Test
    internal fun testPostTraversal() {
        val ctx = StreamContext()
        val defn = addType<GroupTask>(SimpleGroupTask::class).toScope(ScopeType.singleton.name) withId taskId
        ctx.add(defn)
        val handler = GroupTaskHandler(taskId, false, defaultTaskDef)
        handler.postTraversal(ctx)
        assertTrue { ctx.get<SimpleGroupTask>(taskId)?.after ?: false }
    }

    @Test
    internal fun testOnError() {
        val ctx = StreamContext()
        val defn = addType<GroupTask>(SimpleGroupTask::class).toScope(ScopeType.singleton.name) withId taskId
        ctx.add(defn)
        val handler = GroupTaskHandler(taskId, false, defaultTaskDef)
        handler.onError(RuntimeException("test"), ctx)
        assertTrue { ctx.get<SimpleGroupTask>(taskId)?.error ?: false }
    }
}