package org.example

import kotlinx.coroutines.*
import space.kscience.dataforge.data.*
import space.kscience.dataforge.misc.DFExperimental
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class ParallelDeferred<T>(
    private val coroutineContext: CoroutineContext = EmptyCoroutineContext,
    private val dependencies: Iterable<ParallelDeferred<*>> = emptyList(),
    val block: suspend () -> T,
)  {

    private var deferred: Deferred<T>? = null

    @OptIn(DFExperimental::class)
    fun async(coroutineScope: CoroutineScope): Deferred<T> {
        val startedDependencies = dependencies.map { goal ->
            goal.async(coroutineScope)
        }
        return deferred ?: coroutineScope.async(
                coroutineContext
                        + CoroutineMonitor()
                        + Dependencies(startedDependencies)
                        + GoalExecutionRestriction(GoalExecutionRestrictionPolicy.NONE)
        ) {
            block()
        }.also { deferred = it }
    }
}