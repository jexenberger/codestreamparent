package io.codestream.core.runtime

import io.codestream.core.api.CodestreamModule
import io.codestream.core.runtime.modules.system.SystemModule
import java.util.*


object ModuleRegistry {


    internal val modules: MutableMap<ModuleId, CodestreamModule> = mutableMapOf()
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
        val serviceLoader = ServiceLoader.load<CodestreamModule>(io.codestream.core.api.CodestreamModule::class.java)
        for (module in serviceLoader) {
            modules[module.id] = module
        }
    }

    @Synchronized
    operator fun plusAssign(module: CodestreamModule) {
        modules[module.id] = module
    }

    fun getLatestVersion(name:String) : CodestreamModule? {
        return modules
                .entries
                .filter { it.key.name.equals(name) }
                .sortedByDescending { it.value.version }
                .firstOrNull()?.value
    }

    operator fun get(name: ModuleId): CodestreamModule? {
        return if (name.defaultVersion) getLatestVersion(name.name) else modules[name]
    }



}