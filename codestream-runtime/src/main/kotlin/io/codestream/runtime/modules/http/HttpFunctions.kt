package io.codestream.runtime.modules.http

import io.codestream.api.annotations.ModuleFunction
import io.codestream.api.annotations.ModuleFunctionParameter
import java.util.*

class HttpFunctions {

    @ModuleFunction("Generates a basic auth string from the passed parameters")
    fun basicAuth(
            @ModuleFunctionParameter(value = "user", description = "User")
            user: String,
            @ModuleFunctionParameter(value = "password", description = "Password")
            password: String
    ) = Base64.getEncoder().encodeToString("$user:$password".toByteArray())

}