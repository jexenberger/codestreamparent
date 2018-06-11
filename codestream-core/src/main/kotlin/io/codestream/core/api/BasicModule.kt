package io.codestream.core.api

import de.skuzzle.semantic.Version
import io.codestream.core.api.annotations.Parameter
import io.codestream.core.api.annotations.Task
import io.codestream.core.runtime.Type
import io.codestream.di.api.theType
import io.codestream.util.mutableProperties
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.full.findAnnotation

fun typeToDescriptor(module: Module, type: KClass<*>): TaskDescriptor {
    val taskAnnotation = type.findAnnotation<Task>()
            ?: throw ComponentDefinitionException(type.qualifiedName!!, "No @Task annotation present")
    val properties = mutableMapOf<String, ParameterDescriptor>()
    val constructor = io.codestream.di.api.resolveConstructor(type)
    constructor?.let { structor ->
        structor.parameters
                .filter { it.findAnnotation<Parameter>() != null }
                .forEach {

                    it.findAnnotation<Parameter>()?.let { param ->
                        val name = it.name!!
                        checkOptionalRequiredDeclaration(it.isOptional, param, name)
                        properties[name] = createProperty(name, it.type.classifier!!, param)
                    }
                }
    }
    type.mutableProperties
            .filter { it.findAnnotation<Parameter>() != null }
            .forEach { prop ->
                prop.findAnnotation<Parameter>()?.let { param ->
                    val name = prop.name
                    val property = createProperty(name, prop.returnType.classifier!!, param)
                    properties[name] = property
                }
            }
    return TaskDescriptor(module, taskAnnotation.name, taskAnnotation.description, properties.toMap(), theType(type))
}

private fun checkOptionalRequiredDeclaration(optional: Boolean, param: Parameter, name: String) {
    if (!optional && !param.required) {
        throw throw ComponentDefinitionException(name, "Is Non-nullable value but @Parameter is defined as not required")
    }
}

private fun createProperty(name: String, prop: KClassifier, param: Parameter): ParameterDescriptor {
    val propertyType = prop as KClass<*>
    val descriptorType = Type.typeForClass(propertyType)
            ?: throw ComponentDefinitionException(name, "No @Task annotation present")
    val property = ParameterDescriptor(name, param.description, descriptorType, param.required, param.alias, param.allowedValues, param.regex)
    return property
}


class BasicModule(
        override val name: String,
        override val description: String,
        override val version: Version = Version.create(1, 0, 0)) : Module {

    constructor(
            name: String,
            description: String,
            version: Version = Version.create(1, 0, 0),
            builder: BasicModule.() -> Unit
    ) : this(name, description, version) {
        create(builder)
    }

    private val _tasks = LinkedHashMap<String, TaskDescriptor>()

    override val tasks: Map<String, TaskDescriptor>
        get() = _tasks.toMap()

    override fun get(name: String): TaskDescriptor? {
        return tasks[name]
    }

    fun <T : io.codestream.core.api.Task> add(type: KClass<T>) {
        val typeToDescriptor = typeToDescriptor(this, type)
        add(typeToDescriptor)
    }

    fun add(descriptor: TaskDescriptor) {
        this._tasks[descriptor.name] = descriptor
    }


    fun create(handler: BasicModule.() -> Unit) {
        handler()
    }
}
