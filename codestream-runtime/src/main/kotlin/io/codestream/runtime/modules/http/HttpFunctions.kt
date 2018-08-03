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

package io.codestream.runtime.modules.http

import io.codestream.api.annotations.ModuleFunction
import io.codestream.api.annotations.ModuleFunctionParameter
import io.codestream.util.crypto.Secret
import java.util.*

class HttpFunctions {

    @ModuleFunction("Generates a basic auth string from the passed parameters")
    fun basicAuth(
            @ModuleFunctionParameter(value = "user", description = "User")
            user: String,
            @ModuleFunctionParameter(value = "password", description = "Password")
            password: Any): String {

        val pass = if (password is Secret) password.value else password.toString()
        val encoded =  Base64.getEncoder().encodeToString("$user:$pass".toByteArray())
        return "Basic $encoded"
    }

}