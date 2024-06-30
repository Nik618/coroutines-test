package org.example

import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class Dependencies(val values: Collection<Job>) : CoroutineContext.Element {
    override val key: CoroutineContext.Key<*> get() = Dependencies

    companion object : CoroutineContext.Key<Dependencies>
}