package io.codestream.runtime.tree

import io.codestream.api.Directive
import io.codestream.util.system
import java.util.concurrent.*

abstract class Branch<T>(override val id: String,
                      val parallel: Boolean = true) : Node<T> {

    val children: List<Node<T>> get() = _childList

    private val _childList: MutableList<Node<T>> = mutableListOf()
    private var internalState: NodeState = NodeState.unrun

    override val state: NodeState
        get() = synchronized(this) { internalState }


    fun add(child: Node<T>) = _childList.add(child)

    @Synchronized
    operator fun plusAssign(child: Node<T>) {
        add(child)
    }

    @Synchronized
    operator fun plus(child: Node<T>): Branch<T> {
        add(child)
        return this
    }

    override fun nodeById(id: String): Node<T>? {
        return super.nodeById(id)?.let { it } ?: run {
            var found: Node<T>? = null
            val children = this.children.iterator()
            while (children.hasNext() && found == null) {
                found = children.next().nodeById(id)
            }
            return found
        }
    }

    override fun execute(ctx: T): NodeState {
        internalState = NodeState.running
        try {
            enterBranch(ctx)
            var doAgain = true
            while (doAgain) {
                val pre = preTraversal(ctx)
                if (pre != Directive.continueExecution) {
                    internalState = NodeState.completed
                    return internalState
                }
                runChildren(ctx)
                val post = postTraversal(ctx)
                if (post != Directive.again) {
                    doAgain = false
                }
            }
            internalState = NodeState.completed
        } catch (e: Exception) {
            internalState = NodeState.failed
            val ex = if (e is ExecutionException) e.cause as Exception else e
            onError(ex, ctx)
        } finally {
            exitBranch(ctx)
        }
        return internalState
    }

    open protected fun runChildren(ctx: T) {
        val futures = mutableListOf<Future<*>>()
        for (node in _childList) {
            val future: Future<Node<T>> = system.optimizedExecutor.submit(Callable<Node<T>> { runLeafNode(node, ctx) })
            if (parallel) {
                futures += future
            } else {
                future.get()
            }
        }
        futures.forEach { it.get() }
    }

    private fun runLeafNode(node: Node<T>, ctx: T): Node<T> {
        node.execute(ctx)
        return node
    }

    override fun dump(level:Int) {
        for (i in 0..level) {
            print(" ")
        }
        println(id)
        children.forEach { it.dump(level+1) }
    }

    override fun traverse(handler: (Node<T>, Node<T>?) -> Unit) {
        handler(this, null)
        children.forEach { handler(it, this) }
    }

    abstract fun preTraversal(ctx: T): Directive
    abstract fun postTraversal(ctx: T): Directive
    abstract fun onError(error: Exception, ctx: T)
    abstract fun enterBranch(ctx: T)
    abstract fun exitBranch(ctx:T)


}