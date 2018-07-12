package io.codestream.core.runtime.modules.system

import io.codestream.core.api.KotlinModule

class SystemModule : KotlinModule(SystemModule.name,"Core system module") {

    init {
        create {
            add(Echo::class)
            add(Group::class)
            add(ForEach::class)
            add(Set::class)
            add(Script::class)
        }
    }

    companion object {
        val name = "sys"
    }

}