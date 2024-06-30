package org.example

import kotlinx.coroutines.*

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class ParallelDeferred<T>(
    private val coroutineContext: CoroutineContext = EmptyCoroutineContext,
    private val dependencies: Iterable<ParallelDeferred<*>> = emptyList(),
    val block: suspend () -> T,
)  {

    private var deferred: Deferred<T>? = null

    fun async(coroutineScope: CoroutineScope): Deferred<T> {
        val startedDependencies = dependencies.map { goal ->
            goal.async(coroutineScope)
        }
        return deferred ?: coroutineScope.async(
                coroutineContext
                        + Dependencies(startedDependencies)
        ) {
            block()
        }.also { deferred = it }
    }
}