package io.codestream.core.runtime.tree

import java.util.concurrent.*

abstract class Branch<T>(override val id: String,
                      val parallel: Boolean = true,
                      val executorService: ExecutorService = Executors.newFixedThreadPool(2)) : Node<T> {

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
                if (pre != BranchProcessingDirective.continueExecution) {
                    internalState = NodeState.completed
                }
                val futures = mutableListOf<Future<*>>()
                for (node in _childList) {
                    val future: Future<Node<T>> = executorService.submit(Callable<Node<T>> { runLeafNode(node, ctx) })
                    if (parallel) {
                        futures += future
                    } else {
                        future.get()
                    }
                }
                futures.forEach { it.get() }
                val post = postTraversal(ctx)
                if (post != BranchProcessingDirective.again) {
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

    private fun runLeafNode(node: Node<T>, ctx: T): Node<T> {
        node.execute(ctx)
        return node
    }

    abstract fun preTraversal(ctx: T): BranchProcessingDirective
    abstract fun postTraversal(ctx: T): BranchProcessingDirective
    abstract fun onError(error: Exception, ctx: T)
    abstract fun enterBranch(ctx: T)
    abstract fun exitBranch(ctx:T)


}