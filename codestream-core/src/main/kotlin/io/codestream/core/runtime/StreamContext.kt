package io.codestream.core.runtime

import io.codestream.core.api.CodestreamModule
import io.codestream.core.api.Task
import io.codestream.core.api.TaskDoesNotExistException
import io.codestream.core.runtime.metamodel.TaskDef
import io.codestream.core.runtime.container.ParameterDependency
import io.codestream.core.runtime.container.TaskContextDependency
import io.codestream.di.api.*

class StreamContext(val parent:StreamContext? = null) : ApplicationContext(mutableMapOf(), mutableMapOf()) {

    private var _bindings: ScopedDependencyBindings = ScopedDependencyBindings(this)

    override val bindings: ScopedDependencyBindings get() = _bindings

    constructor(theBindings: ScopedDependencyBindings, parent:StreamContext? = null) : this(parent) {
        _bindings = theBindings
    }

    fun registerTask(def:TaskDef, callingModule: CodestreamModule? = null) {
        val descriptor = TaskRegistry.resolve(def.id.taskType, callingModule) ?: throw TaskDoesNotExistException(def.id.taskType)
        bind(descriptor.factory) withId def.id toScope ScopeType.prototype.name into this
        addInstance(def) withId TaskDefId(def.id) into this
    }

    fun registerTask(id:TaskId, task:Task) {
        addInstance(task) withId id  into this
    }

    override fun <T> get(id: ComponentId): T? {
        return super.get(id) ?: parent?.get(id)
    }

    override fun <T> value(key: String): T? {
        return super.value(key) ?: parent?.value(key)
    }

    override fun hasComponent(id: ComponentId): Boolean {
        return if (super.hasComponent(id)) true else parent?.hasComponent(id) ?: false
    }

    fun subContext() = StreamContext(this.bindings.subBindings(), this)


    companion object {
        init {
            addDependencyHandler(ParameterDependency())
            addDependencyHandler(TaskContextDependency())
        }
    }

}