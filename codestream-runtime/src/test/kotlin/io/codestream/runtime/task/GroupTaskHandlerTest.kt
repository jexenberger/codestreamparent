package io.codestream.runtime.task

import io.codestream.api.TaskId
import io.codestream.api.TaskType
import io.codestream.api.metamodel.GroupTaskDef
import io.codestream.api.metamodel.ParameterDef
import io.codestream.runtime.ModuleRegistry
import io.codestream.runtime.SimpleGroupTaskContext
import io.codestream.runtime.StreamContext
import io.codestream.runtime.TestModule
import io.codestream.runtime.container.TaskScope
import io.codestream.runtime.container.TaskScopeId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GroupTaskHandlerTest {

    private val taskId: TaskId

    private val defaultTaskDef: GroupTaskDef

    private val module = TestModule()

    init {
        ModuleRegistry += module
        taskId = TaskId(TaskType(module.id, "group"))
        defaultTaskDef = GroupTaskDef(
                taskId,
                mapOf("value" to ParameterDef("value", "test")),
                false
        )
    }

    @AfterEach
    internal fun tearDown() {
        TaskScope.releaseAll()
    }

    @Test
    internal fun testPreTraversal() {
        val ctx = StreamContext()
        ctx.registerTask(defaultTaskDef)
        val handler = io.codestream.runtime.task.GroupTaskHandler(taskId, false, defaultTaskDef)
        handler.enterBranch(ctx)
        handler.preTraversal(ctx)
        val taskContext = ctx.get<SimpleGroupTaskContext>(TaskScopeId(ctx, taskId))!!
        assertTrue { taskContext.before }
    }

    @Test
    internal fun testPreTraversalConditional() {
        val id = TaskId(TaskType(module.id, "group"))
        val taskDef = GroupTaskDef(
                id,
                mapOf("value" to ParameterDef("value", "test")),
                false,
                { false }
        )
        val ctx = StreamContext()
        ctx.registerTask(taskDef)
        val handler = io.codestream.runtime.task.GroupTaskHandler(id, false, taskDef)
        handler.enterBranch(ctx)
        handler.preTraversal(ctx)
        val taskContext = ctx.get<SimpleGroupTaskContext>(TaskScopeId(ctx, id))
        assertNull(taskContext)
    }

    @Test
    internal fun testPostTraversal() {
        val ctx = StreamContext()
        ctx.registerTask(defaultTaskDef)
        val handler = io.codestream.runtime.task.GroupTaskHandler(taskId, false, defaultTaskDef)
        handler.enterBranch(ctx)
        handler.postTraversal(ctx)
        val taskContext = ctx.get<SimpleGroupTaskContext>(TaskScopeId(ctx, taskId))!!
        assertTrue { taskContext.after }
    }

    @Test
    internal fun testOnError() {
        val ctx = StreamContext()
        ctx.registerTask(defaultTaskDef)
        val handler = io.codestream.runtime.task.GroupTaskHandler(taskId, false, defaultTaskDef)
        handler.enterBranch(ctx)
        handler.onError(RuntimeException("test"), ctx)
        val taskContext = ctx.get<SimpleGroupTaskContext>(TaskScopeId(ctx, taskId))!!
        assertTrue { taskContext.error }
    }
}