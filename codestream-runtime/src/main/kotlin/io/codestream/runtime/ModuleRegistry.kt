package io.codestream.runtime

import io.codestream.api.CodestreamModule
import io.codestream.api.ModuleId
import io.codestream.api.services.ScriptService
import io.codestream.runtime.modules.http.HttpModule
import io.codestream.runtime.modules.resources.ResourcesModule
import io.codestream.runtime.modules.system.SystemModule
import io.codestream.runtime.yaml.DefinedYamlModule
import io.codestream.util.system
import java.io.File
import java.nio.file.Files
import java.util.*


object ModuleRegistry {


    internal val _modules: MutableMap<ModuleId, CodestreamModule> = mutableMapOf()
    internal val systemModule = SystemModule()

    val systemModuleId: ModuleId = systemModule.id

    val modules: Set<ModuleId> get() = _modules.keys.toSet()


    init {
        loadSystemModules()
        load()
    }

    private fun loadSystemModules() {
        this += systemModule
        this += ResourcesModule()
        this += HttpModule()
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
            Files.newDirectoryStream(it.toPath())
                    .filter { it.toFile().isDirectory }
                    .filter { it.toFile().list().contains("module.conf") }
                    .forEach { ModuleRegistry += DefinedYamlModule(it.toFile(), scriptService ) }
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