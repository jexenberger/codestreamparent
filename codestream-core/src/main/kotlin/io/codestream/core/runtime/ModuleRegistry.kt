package io.codestream.core.runtime

import io.codestream.core.runtime.modules.system.SystemModule
import java.util.*


typealias CodeStreamModule = io.codestream.core.api.Module

object ModuleRegistry {


    internal val modules: MutableMap<ModuleId, CodeStreamModule> = mutableMapOf()
    internal val systemModule = SystemModule()

    val systemModuleId:ModuleId = systemModule.id

    init {
        loadSystemModules()
        load()
    }

    private fun loadSystemModules() {
        this += systemModule
    }

    private fun load()  {
        val serviceLoader = ServiceLoader.load<CodeStreamModule>(io.codestream.core.api.Module::class.java)
        for (module in serviceLoader) {
            modules[module.id] = module
        }
    }

    @Synchronized
    operator fun plusAssign(module: CodeStreamModule) {
        modules[module.id] = module
    }

    fun getLatestVersion(name:String) : CodeStreamModule? {
        return modules
                .entries
                .filter { it.key.name.equals(name) }
                .sortedByDescending { it.value.version }
                .firstOrNull()?.value
    }

    operator fun get(name: ModuleId): CodeStreamModule? {
        return if (name.defaultVersion) getLatestVersion(name.name) else modules[name]
    }



}