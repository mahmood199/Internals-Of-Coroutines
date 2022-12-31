package suspension_core

import kotlin.coroutines.CoroutineContext

interface Continuation<in T> {
    val context: CoroutineContext
    fun resume(value: T)
    fun resumeWithException(exception: Throwable)
}