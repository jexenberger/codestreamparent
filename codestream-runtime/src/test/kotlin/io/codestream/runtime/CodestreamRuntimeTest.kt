package io.codestream.runtime

import io.codestream.api.CodestreamSettings
import io.codestream.api.ModuleId
import io.codestream.api.defaultVersion
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class CodestreamRuntimeTest {

    @Test
    internal fun testRunTask() {
        CodestreamRuntime(CodestreamSettings()).runTask(ModuleId("sys", defaultVersion), "echo", mapOf("value" to "hello world"), DefaultParameterCallback())
    }

    @Test
    internal fun testShutdown() {
        val runtime = CodestreamRuntime(CodestreamSettings())
        runtime.runTask(ModuleId("sys", defaultVersion), "echo", mapOf("value" to "hello world"), DefaultParameterCallback())
        //runtime.shutdown()
    }

    @Test
    internal fun testRunGroupTask() {
        CodestreamRuntime(CodestreamSettings()).runTask(ModuleId("sys", defaultVersion), "group", mapOf("value" to "hello world"), DefaultParameterCallback())
    }

    @Test
    internal fun testRunGroupTaskWithCallback() {
        CodestreamRuntime(CodestreamSettings()).runTask(ModuleId("sys", defaultVersion), "echo", emptyMap(), DefaultParameterCallback("red"))
    }

    /*
     need a better test
    @Test
    internal fun testRunGroupTaskWithCallbackEmpty() {
        try {
            CodestreamRuntime(CodestreamSettings()).runTask(ModuleId("sys", defaultVersion), "echo", emptyMap(), DefaultParameterCallback())
            fail("should have failed")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/
}