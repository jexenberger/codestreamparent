package io.codestream.core.runtime.modules.system

import io.codestream.core.api.BasicModule

class SystemModule : BasicModule(SystemModule.name,"Core system module") {

    init {
        create {
            add(Echo::class)
        }
    }

    companion object {
        val name = "sys"
    }

}