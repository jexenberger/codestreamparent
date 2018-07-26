package io.codestream.runtime.modules.json

import io.codestream.api.annotations.ModuleFunction
import io.codestream.api.annotations.ModuleFunctionParameter

class JsonModuleFunctions {

    @ModuleFunction("Takes an array type and writes it in JSON format, returns the value in a String")
    fun arrayToJson(
            @ModuleFunctionParameter("array", "array of values ")
            array:Array<*>
    ) : String {
        return JsonModule.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(array)
    }

    @ModuleFunction("Takes a collection which is Iterable in JSON format, returns the value in a String")
    fun collectionToJson(
            @ModuleFunctionParameter("collection", "collection of values")
            collection:Iterable<*>
    ) : String {
        return JsonModule.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection.iterator())
    }

}