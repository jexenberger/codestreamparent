package io.codestream.core.engine

interface Node {

    val id:String
    val children:List<Node>?
    val isLeaf:Boolean get() = this.children != null
    val state:NodeState get() = NodeState.unrun

    fun execute(ctx: ExecutionContext): NodeState


}