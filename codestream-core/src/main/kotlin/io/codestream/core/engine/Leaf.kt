package io.codestream.core.engine

abstract class Leaf(override val id: String) : Node {
    override val children: List<Node>? get() = null
}