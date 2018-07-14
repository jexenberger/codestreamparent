package io.codestream.runtime.tree

interface NodeProducer<T> {

    fun rootNode() : Node<T>

}