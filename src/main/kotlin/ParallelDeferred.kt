package org.example

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class ParallelDeferred<T>(
    private val childrens: List<ParallelDeferred<*>> = emptyList(),
    val producer: suspend () -> T,
)  {

    suspend fun get(context: CoroutineContext): T {

        val dependencies = childrens.map { children ->
            CoroutineScope(Dispatchers.IO).async {
                children.get(context)
            }
        }

        return CoroutineScope(context).async {
            dependencies.forEach {
                it.join()
            }
            producer()
        }.await()
    }
}
