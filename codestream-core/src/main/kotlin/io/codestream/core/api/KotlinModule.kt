package io.codestream.core.api

import de.skuzzle.semantic.Version
import io.codestream.core.api.CodestreamModule.Companion.defaultVersion
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.annotations.Task
import io.codestream.core.api.descriptor.ParameterDescriptor
import io.codestream.core.api.descriptor.TaskDescriptor
import io.codestream.core.doc.FunctionDoc
import io.codestream.core.runtime.Type
import io.codestream.di.api.theType
import io.codestream.util.ifTrue
import io.codestream.util.mutableProperties
import io.codestream.util.transformation.TransformerService
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSuperclassOf

internal fun <T : io.codestream.core.api.Task> typeToDescriptor(module: CodestreamModule, type: KClass<T>): TaskDescriptor {
    val taskAnnotation = type.findAnnotation<Task>()
            ?: throw ComponentDefinitionException(type.qualifiedName!!, "No @Task annotation present")
    val group = GroupTask::class.isSuperclassOf(type)
    val properties = mutableMapOf<String, ParameterDescriptor>()
    val constructor = io.codestream.di.api.resolveConstructor(type)
    constructor?.let { structor ->
        structor.parameters
                .filter { it.findAnnotation<Parameter>() != null }
                .forEach {

                    it.findAnnotation<Parameter>()?.let { param ->
                        val name = it.name!!
                        checkOptionalRequiredDeclaration(it.type.isMarkedNullable, param, name)
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
    return TaskDescriptor(module, taskAnnotation.name, taskAnnotation.description, properties.toMap(), theType(type), group)
}

internal fun checkOptionalRequiredDeclaration(optional: Boolean, param: Parameter, name: String) {
    if (!optional && !param.required && param.default.isEmpty()) {
        throw throw ComponentDefinitionException(name, "Is Non-nullable value but @Parameter is defined as not required")
    }
}

internal fun createProperty(name: String, propertyType: KClass<*>, param: Parameter): ParameterDescriptor {
    val descriptorType = Type.typeForClass(propertyType)
            ?: throw ComponentDefinitionException(name, "${propertyType.simpleName} is not a supported Parameter type")
    val property = ParameterDescriptor(name, param.description, descriptorType, param.required, param.alias, param.allowedValues, param.regex, param.default)
    return property
}


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

    fun <T : io.codestream.core.api.Task> add(type: KClass<T>) {
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
    }
}
