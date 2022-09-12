package com.example.diplomclient.arch.redux.util

import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit

internal abstract class AbsDummyExecutorService : ExecutorService {

    override fun shutdown(): Unit = Unit

    override fun shutdownNow(): List<Runnable> = emptyList()

    override fun isShutdown(): Boolean = false

    override fun isTerminated(): Boolean = false

    override fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean = false

    override fun <T> submit(task: Callable<T>): Future<T> = FutureTask(task)

    override fun <T> submit(task: Runnable, result: T): Future<T> = FutureTask {
        task.run()
        result
    }

    override fun submit(task: Runnable): Future<*> = FutureTask {
        task.run()
        null
    }

    override fun <T> invokeAll(tasks: Collection<Callable<T>>): List<Future<T>> = emptyList()

    override fun <T> invokeAll(tasks: Collection<Callable<T>>, timeout: Long, unit: TimeUnit): List<Future<T>> = emptyList()

    @Throws(ExecutionException::class)
    override fun <T> invokeAny(tasks: Collection<Callable<T>>): T {
        require(!tasks.isEmpty()) { "tasks is empty" }
        return try {
            tasks.iterator().next().call()!!
        } catch (e: Exception) {
            throw ExecutionException("Callable.call threw exception", e)
        }
    }

    override fun <T> invokeAny(tasks: Collection<Callable<T>>, timeout: Long, unit: TimeUnit): T? = null
}
