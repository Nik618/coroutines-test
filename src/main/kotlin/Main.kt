package org.example

import kotlinx.coroutines.Dispatchers
import kotlin.random.Random


fun test1() {
    println("test1")
}

fun test2() {
    println("test2")
}

fun test3() {
    println("test3")
}

suspend fun main() {
    ParallelDeferred<Unit>(
        listOf(ParallelDeferred<Unit>(
            listOf(),
            ::test1
        ), ParallelDeferred<Unit>(
            listOf(),
            ::test2
        )),
        ::test3
    ).get(Dispatchers.IO)
}
