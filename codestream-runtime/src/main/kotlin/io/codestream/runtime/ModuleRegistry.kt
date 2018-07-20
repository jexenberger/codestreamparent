package io.codestream.runtime

import io.codestream.api.CodestreamModule
import io.codestream.api.ModuleId
import io.codestream.api.services.ScriptService
import io.codestream.runtime.modules.http.HttpModule
import io.codestream.runtime.modules.json.JsonModule
import io.codestream.runtime.modules.resources.ResourcesModule
import io.codestream.runtime.modules.system.SystemModule
import io.codestream.runtime.modules.templating.TemplateModule
import io.codestream.runtime.yaml.DefinedYamlModule
import io.codestream.util.system
import java.io.File
import java.nio.file.Files
import java.util.*


object ModuleRegistry {


    internal val _modules: MutableMap<ModuleId, CodestreamModule> = mutableMapOf()
    internal val systemModule = SystemModule()

    val systemModules = setOf(systemModule, ResourcesModule(), HttpModule(), TemplateModule())

    val systemModuleId: ModuleId = systemModule.id

    val modules: Set<ModuleId> get() = _modules.keys.toSet()

    val systemModuleFunctionObjects: Map<String, Any?> get() {
        return systemModules
                .filter { it.scriptObjectType != null }
                .map {
            "__${it.name}" to it.scriptObject!!
        }.toMap()
    }


    init {
        loadSystemModules()
        load()
    }

    private fun loadSystemModules() {
        systemModules.forEach {
            this += it
        }
    }

    private fun load() {
        val serviceLoader = ServiceLoader.load<CodestreamModule>(io.codestream.api.CodestreamModule::class.java)
        for (module in serviceLoader) {
            _modules[module.id] = module
        }
    }

    @Synchronized
    fun loadDefinedYamlModules(modulePaths: String, scriptService: ScriptService) {
        val paths = system.parsePathPath(modulePaths).map { File(it) }

        paths.filter { it.isDirectory }.forEach {
            it.listFiles()
                    .filter { it.isDirectory }
                    .filter { it.list().contains("module.conf") }
                    .forEach {ModuleRegistry += DefinedYamlModule(it, scriptService) }
        }
    }

    @Synchronized
    operator fun plusAssign(module: CodestreamModule) {
        _modules[module.id] = module
    }

    fun getLatestVersion(name: String): CodestreamModule? {
        return _modules
                .entries
                .filter { it.key.name.equals(name) }
                .sortedByDescending { it.value.version }
                .firstOrNull()?.value
    }

    operator fun get(name: ModuleId): CodestreamModule? {
        return if (name.isDefaultVersion) getLatestVersion(name.name) else _modules[name]
    }


}