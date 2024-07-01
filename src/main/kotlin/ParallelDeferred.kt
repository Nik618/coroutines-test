package org.example

import kotlinx.coroutines.*

open class ParallelDeferred<T>(
    private val childrens: List<ParallelDeferred<*>> = emptyList(),
    val producer: suspend () -> T,
)  {

    private var deferred: Deferred<T>? = null

    suspend fun get(): T {

        val dependencies = childrens.map { children ->
            CoroutineScope(Dispatchers.IO).async {
                children.get()
            }
        }

        deferred = CoroutineScope(Dispatchers.IO).async {
            dependencies.forEach {
                it.await()
            }
            producer()
        }

        return deferred!!.await()
    }
}
