package context_switching.core

import suspension.core.Continuation

internal class DispatchedContinuation<in T>(
    val dispatcher: CoroutineDispatcher,
    val continuation: Continuation<T>
) : Continuation<T> by continuation, DispatchedTask<T> {

}