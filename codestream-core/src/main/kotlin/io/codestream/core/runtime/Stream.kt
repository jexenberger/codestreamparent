package io.codestream.core.runtime

import io.codestream.core.api.TaskContext
import io.codestream.core.runtime.tree.Branch
import io.codestream.core.runtime.tree.DefaultBranch
import io.codestream.core.runtime.tree.Node
import io.codestream.core.runtime.tree.NodeProducer

class Stream(val id: StreamId, val parallel: Boolean = false) : NodeProducer<StreamContext> {

    private val rootNode: Branch<StreamContext> = DefaultBranch(id.toString(), parallel)

    override fun rootNode(): Node<StreamContext> = rootNode

    fun add(node: Node<StreamContext>): Stream {
        this.rootNode += node
        return this
    }

    fun run(ctx: TaskContext) = this.rootNode.execute(StreamContext())

    operator fun plus(node: Node<StreamContext>) = add(node)
}