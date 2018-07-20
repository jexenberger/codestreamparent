package io.codestream.runtime.modules.system

import io.codestream.api.KotlinModule

class SystemModule : KotlinModule(SystemModule.name,"Core system module", scriptObjectType = SystemModuleFunctions::class) {



    init {
        create {
            add(Echo::class)
            add(Group::class)
            add(ForEach::class)
            add(Set::class)
            add(Script::class)
            add(Shell::class)
            add(Fail::class)
        }
    }


    companion object {
        val name = "sys"
    }

}