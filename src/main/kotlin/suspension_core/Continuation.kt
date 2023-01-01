package suspension_core

import kotlin.coroutines.CoroutineContext

/** T is whatever object we want to get in result after the operation
 *  resume
 *  resumeWithException can throw error is `any`
 */

interface Continuation<in T> {
    val context: CoroutineContext
    fun resume(value: T)
    fun resumeWithException(exception: Throwable)
}