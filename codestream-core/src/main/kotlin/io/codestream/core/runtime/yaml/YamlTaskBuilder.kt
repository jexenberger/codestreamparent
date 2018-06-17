package io.codestream.core.runtime.yaml

import com.fasterxml.jackson.module.kotlin.readValue
import io.codestream.core.api.ComponentDefinitionException
import io.codestream.core.api.ParameterDescriptor
import io.codestream.core.api.TaskDescriptor
import io.codestream.core.api.TaskType
import io.codestream.core.metamodel.GroupTaskDef
import io.codestream.core.metamodel.ParameterDef
import io.codestream.core.metamodel.TaskDef
import io.codestream.core.runtime.StreamContext
import io.codestream.core.runtime.TaskId
import io.codestream.core.runtime.Type
import io.codestream.core.runtime.task.GroupTaskHandler
import io.codestream.core.runtime.task.SimpleTaskHandler
import io.codestream.core.runtime.task.defaultCondition
import io.codestream.core.runtime.task.scriptCondition
import io.codestream.core.runtime.tree.Node
import io.codestream.core.runtime.tree.NodeProducer
import java.util.*

class YamlTaskBuilder(val source: String, val module: io.codestream.core.api.Module, yaml: String, val ctx:StreamContext) : NodeProducer<StreamContext>{




    val taskDescriptor by lazy { load() }
    val taskDefMap: Map<String, Any?>

    init {
        taskDefMap = yaml().readValue(yaml)
    }

    override fun rootNode(): Node<StreamContext> {
        val tasks = taskDefMap["tasks"] as Map<String, Any?>? ?: throw ComponentDefinitionException(taskDescriptor.type.taskName, "has no tasks defined")
        val thisTask = defineTask(ctx, taskDescriptor.type,tasks) as GroupTaskHandler
        return defineTaskTree(taskDefMap, ctx, thisTask)
    }


    fun defineTaskTree(taskMap: Map<String, Any?>, ctx: StreamContext, parent: GroupTaskHandler): GroupTaskHandler {
        if (taskMap.isEmpty()) {
            throw ComponentDefinitionException(taskDescriptor.type.taskName, "has no tasks defined")
        }
        val nodes = taskMap.entries.map { (k, v) ->
            val type = TaskType.fromString(k)
            val subTaskMap = v as Map<String, Any?>
            val taskDef = defineTask(ctx, type, subTaskMap)
            val handler = when (taskDef) {
                is GroupTaskDef -> GroupTaskHandler(taskDef.id, taskDef.paralell, taskDef)
                else -> SimpleTaskHandler(taskDef.id, taskDef)
            }
            if (handler is GroupTaskHandler) {
                val nestedTasks = taskMap["tasks"] as Map<String, Any?>?
                        ?: throw ComponentDefinitionException(taskDef.id.toString(), "Group tasks but required zero or more sub-tasks")
                defineTaskTree(nestedTasks, ctx, handler)
            }
            handler

        }
        nodes.forEach { parent += it }
        return parent
    }


    fun defineTask(ctx: StreamContext, taskTaskType: TaskType, taskMap: Map<String, Any?>): TaskDef {
        val reserved = listOf("condition", "onError", "finally", "parallel", "id")
        val simple = !taskMap.containsKey("tasks")
        val params = taskMap.entries
                .filter { !reserved.contains(it.key) }
                .map { ParameterDef(it.key, it.value) }
                .map { it.name to it }
                .toMap()
        val condition = taskMap["condition"]?.let { scriptCondition(it.toString().trim()) } ?: defaultCondition
        val id = taskMap["id"]?.toString() ?: UUID.randomUUID().toString()
        val taskId = TaskId(taskTaskType, id)
        if (simple) {
            return TaskDef(taskId, params, condition)
        }
        val parallel = taskMap["parallel"] as Boolean? ?: false
        val errorTask = taskMap["onError"]?.let {
            extractSingleTask(it, taskId, ctx)
        }
        val finallyTask = taskMap["finally"]?.let {
            extractSingleTask(it, taskId, ctx)
        }
        return GroupTaskDef(taskId, params, parallel, condition, errorTask, finallyTask)
    }

    private fun extractSingleTask(it: Any, taskId: TaskId, ctx: StreamContext): TaskDef {
        val subTaskMap = it as Map<String, Any>
        if (subTaskMap.keys.size != 1) {
            throw ComponentDefinitionException(taskId.stringId, "onError task must have only one valid task definition")
        }
        val taskKey = subTaskMap.keys.first()
        val subTaskType = TaskType.fromString(taskKey)
        return defineTask(ctx, subTaskType, subTaskMap[taskKey] as Map<String, Any?>)
    }

    fun load(): TaskDescriptor {
        val name = taskDefMap["name"]?.toString()
                ?: throw ComponentDefinitionException(module.name, "$source has no 'name' property")
        val description = taskDefMap["description"]?.toString()
                ?: throw ComponentDefinitionException(module.name, "'$source' has no 'description' property")
        val parameters = taskDefMap["parameters"] as Map<String, Any?>?
        val paramDefs = parameters?.map { (key, value) ->
            key to createParameterDescriptor(value, key, name, description)
        }?.toMap() ?: emptyMap()
        return TaskDescriptor(module, name, description, paramDefs, YamlTaskFactory(null, null))
    }

    private fun createParameterDescriptor(value: Any?, key: String, name: String, description: String): ParameterDescriptor {
        val valMap = (value as Map<String, Any?>?)
                ?: throw ComponentDefinitionException(module.name, "parameter '$key' in '$source' is not defined")
        val typeStr = valMap["type"] as String? ?: "string"
        val type = Type.typeForString(typeStr)
                ?: throw ComponentDefinitionException(module.name, "'$typeStr' is an allowed parameter type for parameter '$key' in '$source'")
        val required = valMap["required"] as Boolean? ?: true
        val allowedValues = valMap["allowed"]?.let {
            when (it) {
                is String -> it.toString().split(",").map { str -> str.trim() }.toTypedArray()
                is List<*> -> it.map { v -> v.toString() }.toTypedArray()
                else -> throw ComponentDefinitionException(module.name, "'$source' has no 'description' property")
            }
        } ?: emptyArray()
        val regex = valMap["regex"]?.toString() ?: ""
        return ParameterDescriptor(name, description, type, required, "", allowedValues, regex)
    }


}