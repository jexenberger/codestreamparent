package io.codestream.runtime.modules.system

import de.skuzzle.semantic.Version
import io.codestream.api.ModuleId
import io.codestream.api.annotations.ModuleFunction
import io.codestream.api.annotations.ModuleFunctionParameter
import io.codestream.api.defaultVersion
import io.codestream.runtime.ModuleRegistry

class SystemModuleFunctions {

    @ModuleFunction("Checks if a module exists with a version string")
    fun moduleExists(
            @ModuleFunctionParameter(value = "name", description = "Name of the module to check")
            name:String,
            @ModuleFunctionParameter(value = "version", description = "Version in string format to check")
            version:String
    ) : Boolean {
        val ver = if (version.equals("LATEST", ignoreCase = true)) defaultVersion else Version.parseVersion(version)
        return ModuleRegistry[ModuleId(name, ver)] != null
    }

}