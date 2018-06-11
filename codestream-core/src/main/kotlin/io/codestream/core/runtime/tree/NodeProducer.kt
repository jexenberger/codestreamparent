package io.codestream.core.runtime.tree

interface NodeProducer<T> {

    fun rootNode() : Node<T>

}