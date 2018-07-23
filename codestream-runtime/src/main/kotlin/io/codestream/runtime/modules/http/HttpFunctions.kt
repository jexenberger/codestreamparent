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