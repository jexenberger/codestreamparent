package io.codestream.core.engine

abstract class Branch(override val id: String, parallel: Boolean = true) : Node {

    override val children: List<Node> get() = _childList
    private val _childList: MutableList<Node> = mutableListOf()


    fun add(child: Node) = _childList.add(child)

    operator fun plusAssign(child: Node) {
        add(child)
    }

    operator fun plus(child: Node): Branch {
        add(child)
        return this
    }

    override fun execute(ctx: ExecutionContext): NodeState {
        return NodeState.unrun
    }

    abstract fun before(ctx: ExecutionContext): BranchProcessingDirective
    abstract fun visitLeaf(ctx: ExecutionContext): BranchProcessingDirective


}