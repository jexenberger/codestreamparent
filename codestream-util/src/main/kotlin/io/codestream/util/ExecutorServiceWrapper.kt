package io.codestream.util

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class ExecutorServiceWrapper(val executorService: ExecutorService) : ExecutorService {
    override fun shutdown() {
        executorService.shutdown()
    }

    override fun <T : Any?> submit(task: Callable<T>?): Future<T> {
        return executorService.submit(task)
    }

    override fun <T : Any?> submit(task: Runnable?, result: T): Future<T> {
        return executorService.submit(task, result)
    }

    override fun submit(task: Runnable?): Future<*> {
        return executorService.submit(task)
    }

    override fun shutdownNow(): MutableList<Runnable> {
        return executorService.shutdownNow()
    }

    override fun isShutdown(): Boolean {
        return executorService.isShutdown
    }

    override fun awaitTermination(timeout: Long, unit: TimeUnit?): Boolean {
        return executorService.awaitTermination(timeout, unit)
    }

    override fun <T : Any?> invokeAny(tasks: MutableCollection<out Callable<T>>?): T {
        return executorService.invokeAny(tasks)
    }

    override fun <T : Any?> invokeAny(tasks: MutableCollection<out Callable<T>>?, timeout: Long, unit: TimeUnit?): T {
        return executorService.invokeAny(tasks, timeout, unit)
    }

    override fun isTerminated(): Boolean {
        return executorService.isTerminated
    }

    override fun <T : Any?> invokeAll(tasks: MutableCollection<out Callable<T>>?): MutableList<Future<T>> {
        return executorService.invokeAll(tasks)
    }

    override fun <T : Any?> invokeAll(tasks: MutableCollection<out Callable<T>>?, timeout: Long, unit: TimeUnit?): MutableList<Future<T>> {
        return executorService.invokeAll(tasks, timeout, unit)
    }

    override fun execute(command: Runnable?) {
        executorService.execute(command)
    }
}