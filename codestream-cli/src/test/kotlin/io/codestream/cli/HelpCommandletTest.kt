package io.codestream.cli

import io.codestream.api.CodestreamSettings
import io.codestream.runtime.CodestreamRuntime
import org.junit.jupiter.api.Test

class HelpCommandletTest {

    @Test
    internal fun testModuleHelp() {
        val tsk = HelpCommandlet(CodestreamRuntime(CodestreamSettings()), "modules")
        tsk.run()
    }

    @Test
    internal fun testModuleCompleteHelp() {
        val tsk = HelpCommandlet(CodestreamRuntime(CodestreamSettings()), "sys")
        tsk.run()
    }

    @Test
    internal fun testModuleCompleteHelpWithVersion() {
        val tsk = HelpCommandlet(CodestreamRuntime(CodestreamSettings()), "sys@LATEST")
        tsk.run()
    }

    @Test
    internal fun testTaskHelp() {
        val tsk = HelpCommandlet(CodestreamRuntime(CodestreamSettings()), "sys@LATEST::echo")
        tsk.run()
    }

    @Test
    internal fun testTaskWithReturnTypeHelp() {
        val tsk = HelpCommandlet(CodestreamRuntime(CodestreamSettings()), "sys@LATEST::script")
        tsk.run()
    }
}