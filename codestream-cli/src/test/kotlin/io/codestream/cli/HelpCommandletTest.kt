/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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