package org.example

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlin.coroutines.EmptyCoroutineContext

fun test(): String {
    println("test")
    return "test"
}

fun parent(): ParallelDeferred<String> {
    println("test2")
    return ParallelDeferred(
        EmptyCoroutineContext,
        listOf(),
        ::test
    )
}

@OptIn(DelicateCoroutinesApi::class)
suspend fun main() {
    ParallelDeferred<ParallelDeferred<String>>(
        EmptyCoroutineContext,
        listOf(ParallelDeferred<String>(
            EmptyCoroutineContext,
            listOf(),
            ::test
        ), ParallelDeferred<String>(
            EmptyCoroutineContext,
            listOf(),
            ::test
        )),
        ::parent
    ).async(GlobalScope).await()
}