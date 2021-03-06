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

package io.codestream.runtime.yaml

import io.codestream.api.*
import io.codestream.api.descriptor.ParameterDescriptor
import io.codestream.api.descriptor.TaskDescriptor
import io.codestream.api.metamodel.FunctionalTaskDef
import io.codestream.runtime.CompositeTask
import io.codestream.runtime.StreamContext
import io.codestream.api.metamodel.GroupTaskDef
import io.codestream.api.metamodel.ParameterDef
import io.codestream.api.metamodel.TaskDef
import io.codestream.runtime.task.*
import io.codestream.runtime.tree.Branch
import io.codestream.util.system
import io.codestream.util.transformation.TransformerService
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.nodes.MappingNode
import org.yaml.snakeyaml.nodes.Node
import org.yaml.snakeyaml.nodes.ScalarNode
import org.yaml.snakeyaml.nodes.SequenceNode
import java.io.File
import java.io.StringReader
import java.util.concurrent.ExecutorService

@Suppress("UNCHECKED_CAST")
class YamlTaskBuilder(val source: String, val module: BaseYamlModule, yaml: String, val executorService: ExecutorService = system.optimizedExecutor) {


    val taskDescriptor by lazy { load() }
    val taskDefMap: ParsedMap<String, Any?>
    val taskLoadMap: MutableMap<String, Pair<Int, Any?>> = mutableMapOf()

    init {


        val document: MappingNode = Yaml().compose(StringReader(yaml)) as MappingNode
        val res = processNode(0, document)
        taskDefMap = res as ParsedMap<String, Any?>
    }


    private fun processNode(depth: Int, node: Node): Any {
        return when (node) {
            is ScalarNode -> processScalarNode(depth, node)
            is MappingNode -> processMappingNode(depth, node)
            is SequenceNode -> processSequenceNode(depth, node)
            else -> throw IllegalStateException("$node not recognised")

        }
    }

    fun processMappingNode(depth: Int, node: MappingNode): ParsedMap<String, Any?> {
        val lineNumber = node.startMark.line
        val ret = ParsedMap<String, Any?>(lineNumber)
        node.value.forEach {
            val tagNode = it.keyNode as ScalarNode
            val valueNode = it.valueNode
            val tag = tagNode.value
            ret[tag] = processNode(depth + 1, valueNode)
        }
        return ret
    }

    fun processScalarNode(depth: Int, node: ScalarNode): Value {
        return Value(node.startMark.line, node.value)
    }

    fun processSequenceNode(depth: Int, node: SequenceNode): ParsedList<Any?> {
        val seqList = ParsedList<Any?>(node.startMark.line)
        node.value.forEach { seqNode ->
            seqList += processNode(depth + 1, seqNode)
        }
        return seqList
    }

    fun defineTaskTree(parent: CompositeTask) {
        val tasks = taskDefMap["tasks"] as List<Map<String, Any?>>?
                ?: throw ComponentDefinitionException(source, "Group tasks but required zero or more sub-tasks")
        defineTaskTree(tasks, parent)
        (taskDefMap["return"] as Map<String, Any?>?)?.let {
            val variableName = (it["variable"] as Value?)?.toString()
                    ?: throw ComponentDefinitionException(source, "Return type defined but no return variable name defined")
            val type = (it["type"] as Value?)?.let { Type.valueOf(it.toString()) } ?: Type.string
            parent.returnVal = type to variableName
        }

        taskDefMap["onError"]?.let {
            val taskDef = extractSingleTask(it, source)
            parent.errorTask = NonGroupTaskHandler(taskDef.id, taskDef)
        }
        taskDefMap["finally"]?.let {
            val taskDef = extractSingleTask(it, source)
            parent.finallyTask = NonGroupTaskHandler(taskDef.id, taskDef)
        }
    }

    fun defineTaskTree(taskMap: List<Map<String, Any?>>, parent: Branch<StreamContext>): Branch<StreamContext> {
        if (taskMap.isEmpty()) {
            throw ComponentDefinitionException(source, "has no tasks defined")
        }
        val nodes = taskMap.map {
            val key = it.keys.iterator().next()
            val type = TaskType.fromString(key)
            @Suppress("UNCHECKED_CAST")
            val subTaskMap = it[key] as ParsedMap<String, Any?>
            val taskDef = defineTask(type, subTaskMap)
            val handler = when (taskDef) {
                is GroupTaskDef -> GroupTaskHandler(taskDef.id, taskDef.paralell, taskDef)
                else -> NonGroupTaskHandler(taskDef.id, taskDef)
            }
            if (handler is GroupTaskHandler) {
                val nestedTasks = subTaskMap["tasks"] as List<Map<String, Any?>>?
                        ?: throw ComponentDefinitionException(taskDef.id.toString(), "Group tasks require a 'tasks' definition")
                defineTaskTree(nestedTasks, handler)
            }
            handler

        }
        nodes.forEach { parent += it }
        return parent
    }


    fun defineTask(taskTaskType: TaskType, taskMap: ParsedMap<String, Any?>): TaskDef {
        val reserved = listOf("condition", "onError", "finally", "parallel", "id")
        val simple = !taskMap.containsKey("tasks")
        val params = taskMap.entries
                .filter { !reserved.contains(it.key) }
                .map {(key, value) ->
                    val valueToUse = when(value) {
                        is Value -> value.value
                        is Map<*,*> -> value.mapValues { if (it.value is Value) it.value.toString() else it }
                        else -> value
                    }
                    ParameterDef(key, valueToUse)
                }
                .map { it.name to it }
                .toMap()
        val condition = taskMap["condition"]?.let { scriptCondition(it.toString().trim()) } ?: defaultCondition
        val assign = taskMap["assign"]?.toString()
        val id = "${this.module.name}::${source.substringAfterLast("/")}.yaml@${taskMap.lineNo}"
        val taskId = TaskId(taskTaskType, id)
        if (simple) {
            return if (assign != null) FunctionalTaskDef(taskId, params, condition, assign) else TaskDef(taskId, params, condition)
        }
        val parallel = taskMap["parallel"] as Boolean? ?: false
        return GroupTaskDef(taskId, params, parallel, condition)
    }

    private fun extractSingleTask(it: Any, taskId: String): TaskDef {
        val subTaskMap = it as Map<String, Any>
        if (subTaskMap.keys.size != 1) {
            throw ComponentDefinitionException(taskId, "onError task must have only one valid task definition")
        }
        val taskKey = subTaskMap.keys.first()
        val subTaskType = TaskType.fromString(taskKey)
        return defineTask(subTaskType, subTaskMap[taskKey] as ParsedMap<String, Any?>)
    }

    fun load(): TaskDescriptor {
        val name = File(source).nameWithoutExtension
        val descriptionVal = taskDefMap["description"] as Value?
                ?: throw ComponentDefinitionException(module.name, "'$source' has no 'description' property")
        val parameters = taskDefMap["parameters"] as ParsedMap<String, Any?>?
        val paramDefs = parameters?.map { (key, value) ->
            key to createParameterDescriptor(value, key, name)
        }?.toMap() ?: emptyMap()
        val returnDef = (taskDefMap["return"] as Map<String, Any?>?)?.let {
            val description = it["description"]?.toString()
                    ?: throw ComponentDefinitionException(source, "Return type defined but no return variable description defined")
            val type = it["type"]?.let { value -> Type.typeForString(value.toString()) } ?: Type.string
            type to description
        }
        return TaskDescriptor(module = module,
                name = name,
                description = descriptionVal.value.toString(),
                parameters = paramDefs,
                factory = YamlTaskFactory(module),
                groupTask = false,
                returnDescriptor = returnDef)
    }

    fun createParameterDescriptor(value: Any?, key: String, name: String): ParameterDescriptor {
        val valMap = (value as Map<String, Any?>?)
                ?: throw ComponentDefinitionException(module.name, "parameter '$key' in '$source' is not defined")
        val typeStr = valMap["type"] as Value?
        val description = valMap["description"] as Value?
                ?: throw ComponentDefinitionException(module.name, "parameter '$key' in '$source' must contain a description")
        val type = typeStr?.let {
            Type.typeForString(it.value.toString())
                    ?: throw ComponentDefinitionException(module.name, "'$typeStr' is not an allowed parameter type for parameter '$key' in '$source'")
        } ?: Type.string

        val requiredVal = valMap["required"] as Value? ?: Value(0, "true")
        val required = TransformerService.convert<Boolean>(requiredVal.value, Boolean::class)
        val allowedValues = valMap["allowed"]?.let {
            when (it) {
                is Value -> it.value.toString().split(",").map { str -> str.trim() }.toTypedArray()
                is List<*> -> it.map { v -> (v as Value).value.toString() }.toTypedArray()
                else -> throw ComponentDefinitionException(module.name, "'$source' has no 'description' property")
            }
        } ?: emptyArray()
        val regex = valMap["regex"]?.toString() ?: ""
        val default = valMap["default"]?.toString()
        return ParameterDescriptor(key, description.value.toString(), type, required, "", allowedValues, regex, default)
    }


}