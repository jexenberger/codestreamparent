package io.codestream.core.runtime

import io.codestream.core.api.Codestream
import io.codestream.core.api.CodestreamModule.Companion.defaultVersion
import io.codestream.core.api.ModuleId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class CodestreamRuntimeTest {

    @Test
    internal fun testRunTask() {
        Codestream.get().runTask(ModuleId("sys", defaultVersion), "echo", mapOf("value" to "hello world"), DefaultParameterCallback())
    }

    @Test
    internal fun testRunGroupTask() {
        Codestream.get().runTask(ModuleId("sys", defaultVersion), "group", mapOf("value" to "hello world"), DefaultParameterCallback())
    }

    @Test
    internal fun testRunGroupTaskWithCallback() {
        Codestream.get().runTask(ModuleId("sys", defaultVersion), "echo", emptyMap(), DefaultParameterCallback("red"))
    }

    @Test
    internal fun testRunGroupTaskWithCallbackEmpty() {
        try {
            Codestream.get().runTask(ModuleId("sys", defaultVersion), "echo", emptyMap(), DefaultParameterCallback())
            fail("should have failed")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}