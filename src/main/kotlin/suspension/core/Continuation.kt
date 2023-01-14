package suspension.core

import kotlin.coroutines.CoroutineContext

/** T is whatever object we want to get in result after the operation
 *  resume
 *  resumeWithException can throw error is `any`
 */

interface Continuation<in T> {
    /**
     * Context of the coroutine that corresponds to this continuation.
     */
    val context: CoroutineContext

    /**
     * Resumes the execution of the corresponding coroutine by passing
     * [value] as the return value of the last suspension point.
     */
    fun resume(value: T)

    /**
     * Resumes the execution of the corresponding coroutine
     * so that the [exception] is re-thrown right after the
     * last suspension point.
     */
    fun resumeWithException(exception: Throwable)
}