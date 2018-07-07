package io.codestream.core.api

import de.skuzzle.semantic.Version
import io.codestream.core.api.annotations.ModuleFunction
import io.codestream.core.api.annotations.ModuleFunctionParameter
import io.codestream.core.api.descriptor.TaskDescriptor
import io.codestream.core.doc.FunctionDoc
import io.codestream.core.doc.ModuleDoc
import io.codestream.core.doc.ParameterDoc
import io.codestream.core.doc.TaskDoc
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation

interface CodestreamModule {

    val name: String
    val description: String
    val version: Version

    val id: ModuleId
        get() = ModuleId(name, version)

    val scriptObjectDocumentation:Collection<FunctionDoc>

    val taskDocumentation:Collection<TaskDoc> get() {
        return this.tasks.map {
            return tasks.map { (k, v) ->
                val paramDocs = v.parameters
                        .map { (name, parm) -> ParameterDoc(name, parm.description, parm.type.name, parm.required, parm.regex) }
                        .toSet()
                        .sortedBy { it.name }
                TaskDoc(k.name, v.description, paramDocs)
            }.toSet().sortedBy { it.name }
        }
    }

    val documentation:ModuleDoc get() = ModuleDoc(id, description, taskDocumentation, scriptObjectDocumentation)

    val scriptObject:Any?

    val tasks: Map<TaskType, TaskDescriptor>

    operator fun get(name: TaskType): TaskDescriptor?


    fun getByName(name:String) = tasks[TaskType(id.name, name)]

    val dependencies: Set<ModuleId> get() = emptySet()

    fun getDependencyVersion(moduleName: String): Version {
        return dependencies.find { it.name.equals(moduleName) }?.version ?: CodestreamModule.defaultVersion
    }




    companion object {
        val defaultVersion = Version.create(999999999, 999999999, 999999999)

        fun versionString(version: Version) = if (version.equals(defaultVersion)) "LATEST" else version.toString()

        fun functionsDocumentationFromType(it: KClass<*>, module: CodestreamModule): List<FunctionDoc> {
            return it.declaredFunctions
                    .filter { it.visibility!!.equals(KVisibility.PUBLIC) }
                    .map {
                        val description = it.findAnnotation<ModuleFunction>()
                                ?: throw ModuleException(module, "Public Function '${it.name}' is not annotated with @ModuleFunction")
                        FunctionDoc(it.name, description.value, it.parameters.drop(1).map { p ->
                            val desc = p.findAnnotation<ModuleFunctionParameter>()
                                    ?: throw ModuleException(module, "Public Function '${it.name}.${p.name}' is not annotated with @ModuleFunctionParameter")
                            desc.value to desc.description
                        })
                    }
        }

    }
}