package io.codestream.runtime

import io.codestream.api.*
import io.codestream.api.metamodel.TaskDef
import io.codestream.api.services.ScriptService
import io.codestream.runtime.container.ParameterDependency
import io.codestream.runtime.container.TaskContextDependency
import io.codestream.runtime.yaml.BaseYamlModule
import io.codestream.di.api.*
import io.codestream.runtime.services.CodestreamScriptingService
import java.util.*

class StreamContext(
        val parent:StreamContext? = null,
        val id:String = UUID.randomUUID().toString(),
        val originatingContextId:String? = parent?.originatingContextId,
        val scriptingService: ScriptService = CodestreamScriptingService()) : ApplicationContext(mutableMapOf(), mutableMapOf()){

    private var _bindings: io.codestream.runtime.ScopedDependencyBindings = io.codestream.runtime.ScopedDependencyBindings(this)

    override val bindings: io.codestream.runtime.ScopedDependencyBindings get() = _bindings


    init {
        StreamContext += this
        addInstance(scriptingService) withId TypeId(ScriptService::class) into this
    }

    constructor(theBindings: io.codestream.runtime.ScopedDependencyBindings, parent:StreamContext? = null) : this(parent) {
        _bindings = theBindings
    }

    fun registerTask(def:TaskDef, callingModule: CodestreamModule? = null) {
        val descriptor = io.codestream.runtime.TaskRegistry.resolve(def.id.taskType, callingModule) ?: throw TaskDoesNotExistException(def.id.taskType)
        bind(descriptor.factory) withId def.id toScope ScopeType.prototype.name into this
        addInstance(def) withId io.codestream.runtime.TaskDefId(def.id) into this
    }

    fun registerTask(id: TaskId, task:Task) {
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


        private val runningContexts: MutableMap<String, StreamContext> = mutableMapOf()
        val contexts = runningContexts.values.toList()

        @Synchronized
        private operator fun plusAssign(ctx:StreamContext) {
            runningContexts[ctx.id] = ctx
        }

        operator fun get(id: String) = runningContexts[id]

        @Synchronized
        fun release(id:String) {
            runningContexts.remove(id)
        }

    }

}