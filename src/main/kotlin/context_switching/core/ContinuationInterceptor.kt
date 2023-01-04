package context_switching.core

import suspension.core.Continuation
import kotlin.coroutines.CoroutineContext

// Any impl of CoroutineContext acts as a map of element of
// type CoroutineContext.Element each having its own unique key.

interface ContinuationInterceptor : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<ContinuationInterceptor>
    fun <T : Any> interceptContinuation(cont: Continuation<T>): Continuation<T>
}