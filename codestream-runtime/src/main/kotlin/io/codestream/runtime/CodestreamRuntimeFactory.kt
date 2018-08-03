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

package io.codestream.runtime

import io.codestream.api.Codestream
import io.codestream.api.CodestreamFactory
import io.codestream.api.CodestreamSettings
import io.codestream.api.RunContext

class CodestreamRuntimeFactory: CodestreamFactory {

    override fun get(settings: CodestreamSettings): Codestream {
        return CodestreamRuntime(settings)
    }

    override fun get() : Codestream {
        return get(CodestreamSettings())
    }

    override fun runContext() : RunContext {
        return StreamContext().bindings
    }
}