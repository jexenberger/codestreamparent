/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.codestream.api

import de.skuzzle.semantic.Version
import io.codestream.api.annotations.Parameter
import io.codestream.api.descriptor.ParameterDescriptor
import io.codestream.api.descriptor.TaskDescriptor
import io.codestream.di.api.theType
import io.codestream.doc.FunctionDoc
import io.codestream.util.mutableProperties
import kotlin.reflect.KClass
import kotlin.reflect.full.*


open class KotlinModule(
        override val name: String,
        override val description: String,
        override val version: Version = resolveVersion(this::class),
        val scriptObjectType: KClass<*>? = null) : CodestreamModule {


    constructor(
            name: String,
            description: String,
            version: Version = Version.create(1, 0, 0),
            builder: KotlinModule.() -> Unit
    ) : this(name, description, version) {
        create(builder)
    }


    private val _tasks = LinkedHashMap<TaskType, TaskDescriptor>()

    override val scriptObjectDocumentation: Collection<FunctionDoc>
        get() {
            return scriptObjectType?.let { CodestreamModule.functionsDocumentationFromType(it, this) } ?: emptyList()
        }

    override val scriptObject by lazy { scriptObjectType?.createInstance() }

    override val tasks: Map<TaskType, TaskDescriptor>
        get() = _tasks.toMap()

    override fun get(name: TaskType): TaskDescriptor? {
        return tasks[name]
    }

    fun <T : Task> add(type: KClass<T>) {
        val typeToDescriptor = typeToDescriptor(this, type)
        add(typeToDescriptor)
    }

    fun add(descriptor: TaskDescriptor) {
        this._tasks[descriptor.type] = descriptor
    }


    fun create(handler: KotlinModule.() -> Unit) {
        handler()
    }

    companion object {
        fun resolveVersion(type: KClass<*>): Version {
            return type.java.module?.descriptor?.version()?.map {
                val v = if (it.toString().endsWith("-SNAPSHOT")) {
                    val baseVersion = it.toString().split("-")[0]
                    if (baseVersion.split("\\.").size < 2) {
                        "$baseVersion.0"
                    } else {
                        baseVersion
                    }
                } else {
                    it.toString()
                }
                Version.parseVersion(v)
            }?.orElse(defaultVersion) ?: defaultVersion
        }

        fun <T : Task> typeToDescriptor(module: CodestreamModule, type: KClass<T>): TaskDescriptor {
            val taskAnnotation = type.findAnnotation<io.codestream.api.annotations.Task>()
                    ?: throw ComponentDefinitionException(type.qualifiedName!!, "No @Task annotation present")
            val group = GroupTask::class.isSuperclassOf(type)
            val functionalTask = FunctionalTask::class.isSuperclassOf(type)
            val properties = mutableMapOf<String, ParameterDescriptor>()
            val constructor = io.codestream.di.api.resolveConstructor(type)
            constructor?.let { structor ->
                structor.parameters
                        .forEach {
                            it.findAnnotation<Parameter>()?.let { param ->
                                val name = it.name!!
                                checkOptionalRequiredDeclaration(type, it.type.isMarkedNullable, param, name)
                                properties[name] = createProperty(name, it.type.classifier!! as KClass<*>, param)
                            }
                        }
            }
            type.mutableProperties
                    .filter { it.findAnnotation<Parameter>() != null }
                    .forEach { prop ->
                        prop.findAnnotation<Parameter>()?.let { param ->
                            val name = prop.name
                            val property = createProperty(name, prop.returnType.classifier!! as KClass<*>, param)
                            properties[name] = property
                        }
                    }
            val returnDescriptor = if (functionalTask) {
                val returnType = type.functions.find { it.name.equals("evaluate") }
                        ?: throw ComponentDefinitionException(type.qualifiedName!!, "Functional task but does not implement evaluate??!!")
                val typeToUse = Type.typeForClass(returnType.returnType.javaClass.kotlin) ?: Type.any
                val description = taskAnnotation.returnDescription
                typeToUse to description
            } else null
            return TaskDescriptor(module, taskAnnotation.name, taskAnnotation.description, properties.toMap(), theType(type), group, returnDescriptor)
        }

        internal fun checkOptionalRequiredDeclaration(type:KClass<*>, optional: Boolean, param: Parameter, name: String) {
            if (!optional && !param.required && param.default.isEmpty()) {
                throw ComponentDefinitionException(name, "Is Non-nullable value but @Parameter is defined as not required on '${type.qualifiedName}'")
            }
        }

        fun createProperty(name: String, propertyType: KClass<*>, param: Parameter): ParameterDescriptor {
            val descriptorType = Type.typeForClass(propertyType)
                    ?: throw ComponentDefinitionException(name, "${propertyType.simpleName} is not a supported Parameter type")
            val allowedValues = if (propertyType.isSubclassOf(Enum::class)) {
                propertyType.java.enumConstants.map { it.toString() }.toTypedArray()
            } else {
                param.allowedValues
            }
            val property = ParameterDescriptor(name, param.description, descriptorType, param.required, param.alias, allowedValues, param.regex, param.default)
            return property
        }

    }
}
