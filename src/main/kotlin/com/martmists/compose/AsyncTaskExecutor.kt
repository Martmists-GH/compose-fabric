package com.martmists.compose

import kotlinx.coroutines.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.time.Duration.Companion.milliseconds

class AsyncTaskExecutor {
    private val queue = ConcurrentLinkedQueue<Pair<suspend CoroutineScope.() -> Any?, CompletableFuture<Any?>>>()

    fun process() {
        runBlocking {
            coroutineScope {
                while (true) {
                    val entry = queue.poll()
                    if (entry == null) {
                        delay(25.milliseconds)
                        continue
                    }

                    val (task, fut) = entry

                    launch {
                        try {
                            fut.complete(task())
                        } catch (e: Exception) {
                            fut.completeExceptionally(e)
                        }
                    }
                }
            }
        }
    }

    fun <T> submitCatching(block: suspend CoroutineScope.() -> T) = submit {
        try {
            block()
        } catch (e: Exception) {
            if (e !is CancellationException) {
                e.printStackTrace()
            }
        }
    }
    fun <T> submitCatching(dispatcher: CoroutineDispatcher, block: suspend CoroutineScope.() -> T) = submit(dispatcher) {
        try {
            block()
        } catch (e: Exception) {
            if (e !is CancellationException) {
                e.printStackTrace()
            }
        }
    }

    fun <T> submit(block: suspend CoroutineScope.() -> T): CompletableFuture<T> {
        val fut = CompletableFuture<T>()
        @Suppress("UNCHECKED_CAST")
        queue.add((block to fut) as Pair<suspend CoroutineScope.() -> Any?, CompletableFuture<Any?>>)
        return fut
    }

    fun <T> submit(dispatcher: CoroutineDispatcher, block: suspend CoroutineScope.() -> T): CompletableFuture<T> = submit {
        withContext(dispatcher, block)
    }
}
