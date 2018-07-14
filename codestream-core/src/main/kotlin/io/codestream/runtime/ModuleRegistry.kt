package io.codestream.runtime

import de.skuzzle.semantic.Version
import io.codestream.api.CodestreamModule
import io.codestream.api.ModuleId
import io.codestream.runtime.modules.system.SystemModule
import java.util.*


object ModuleRegistry {


    internal val _modules: MutableMap<ModuleId, CodestreamModule> = mutableMapOf()
    internal val systemModule = SystemModule()

    val systemModuleId: ModuleId = systemModule.id

    val modules:Set<Pair<String, Version>> get() =  _modules.map { it.key.name to it.key.version }.toSet()


    init {
        loadSystemModules()
        load()
    }

    private fun loadSystemModules() {
        this += systemModule
    }

    private fun load()  {
        val serviceLoader = ServiceLoader.load<CodestreamModule>(io.codestream.api.CodestreamModule::class.java)
        for (module in serviceLoader) {
            _modules[module.id] = module
        }
    }

    @Synchronized
    operator fun plusAssign(module: CodestreamModule) {
        _modules[module.id] = module
    }

    fun getLatestVersion(name:String) : CodestreamModule? {
        return _modules
                .entries
                .filter { it.key.name.equals(name) }
                .sortedByDescending { it.value.version }
                .firstOrNull()?.value
    }

    operator fun get(name: ModuleId): CodestreamModule? {
        return if (name.defaultVersion) getLatestVersion(name.name) else _modules[name]
    }



}