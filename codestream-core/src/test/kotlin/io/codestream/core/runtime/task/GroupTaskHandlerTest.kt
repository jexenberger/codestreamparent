package io.codestream.core.runtime.task

import io.codestream.core.runtime.SimpleGroupTaskContext
import io.codestream.core.runtime.TestModule
import io.codestream.core.api.TaskError
import io.codestream.core.api.TaskType
import io.codestream.core.runtime.metamodel.GroupTaskDef
import io.codestream.core.runtime.metamodel.ParameterDef
import io.codestream.core.runtime.*
import io.codestream.core.runtime.container.TaskScope
import io.codestream.core.runtime.container.TaskScopeId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GroupTaskHandlerTest {

    private val taskId = TaskId(TaskType("test", "group"))

    private val defaultTaskDef = GroupTaskDef(
            taskId,
            mapOf("value" to ParameterDef("value", "test")),
            false
    )

    init {
        ModuleRegistry += TestModule()
    }

    @AfterEach
    internal fun tearDown() {
        TaskScope.releaseAll()
    }

    @Test
    internal fun testPreTraversal() {
        val ctx = StreamContext()
        ctx.registerTask(defaultTaskDef)
        val handler = GroupTaskHandler(taskId, false, defaultTaskDef)
        handler.enterBranch(ctx)
        handler.preTraversal(ctx)
        val taskContext = ctx.get<SimpleGroupTaskContext>(TaskScopeId(ctx, taskId))!!
        assertTrue { taskContext.before }
    }

    @Test
    internal fun testPreTraversalConditional() {
        val id = TaskId(TaskType("test", "group"))
        val taskDef = GroupTaskDef(
                id,
                mapOf("value" to ParameterDef("value", "test")),
                false,
                { false }
        )
        val ctx = StreamContext()
        ctx.registerTask(taskDef)
        val handler = GroupTaskHandler(id, false, taskDef)
        handler.enterBranch(ctx)
        handler.preTraversal(ctx)
        val taskContext = ctx.get<SimpleGroupTaskContext>(TaskScopeId(ctx, id))
        assertNull(taskContext)
    }

    @Test
    internal fun testPostTraversal() {
        val ctx = StreamContext()
        ctx.registerTask(defaultTaskDef)
        val handler = GroupTaskHandler(taskId, false, defaultTaskDef)
        handler.enterBranch(ctx)
        handler.postTraversal(ctx)
        val taskContext = ctx.get<SimpleGroupTaskContext>(TaskScopeId(ctx, taskId))!!
        assertTrue { taskContext.after }
    }

    @Test
    internal fun testOnError() {
        val ctx = StreamContext()
        ctx.registerTask(defaultTaskDef)
        val handler = GroupTaskHandler(taskId, false, defaultTaskDef)
        handler.enterBranch(ctx)
        handler.onError(TaskError(RuntimeException("test")), ctx)
        val taskContext = ctx.get<SimpleGroupTaskContext>(TaskScopeId(ctx, taskId))!!
        assertTrue { taskContext.error }
    }
}