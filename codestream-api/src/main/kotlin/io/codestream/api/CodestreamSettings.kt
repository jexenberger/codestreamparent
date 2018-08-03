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

package io.codestream.api

import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Qualified
import io.codestream.di.annotation.Value
import java.io.File
import io.codestream.util.OS
import io.codestream.util.crypto.SimpleSecretStore
import io.codestream.util.system
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

data class CodestreamSettings(
        @Value("yaml.module.path")
        val yamlModulePath: String = system.pathString("modules", "${system.homeDir}/.cs/_modules", "."),
        @Inject
        val executor: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
        @Value("enable.debug")
        val debug: Boolean = false,
        @Value("resources.path")
        val resourceRepositoryPath: File = File("${OS.os().homeDir}/.cs/resources"),
        @Value("secretstore.path")
        val secretStorePath: File = File("${OS.os().homeDir}/.cs/secretstore.yaml"),
        @Value("global.key.path")
        val globalKeyPath: File = File("${OS.os().homeDir}/.cs/globalkey.p12")

)
