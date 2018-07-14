package samplemodule.scripts

import io.codestream.api.annotations.ModuleFunction
import io.codestream.api.annotations.ModuleFunctionParameter

class Sample {
    @ModuleFunction("A simple hello world")
    def helloWorld() {
        return "hello world from a nested script"
    }

    @ModuleFunction("the qwerty function that uppercases fredperte")
    def qwerty(@ModuleFunctionParameter(value = "fredperte", description = "The fredperte factor") fredperte) {
        return fredperte?.toString()?.toUpperCase()
    }
}