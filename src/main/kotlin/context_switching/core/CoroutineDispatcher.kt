package context_switching.core

import suspension.core.Continuation
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

// This abstract class is used for the implementation of all dispatchers
// like CommonPool,
// Unconfined,
// and DefaultDispatcher.

abstract class CoroutineDispatcher :
    AbstractCoroutineContextElement(ContinuationInterceptor),
    ContinuationInterceptor {

    //If
    // 1.this returns false then context switching doesn't happen.
    //      and execution of the continuation will happen on the thread
    //      where it was actually executing.
    // 2.this returns true, then only dispatch will be called
    open fun isDispatchNeeded(context: CoroutineContext): Boolean = true

    //This function does the context switching, if isDispatchNeeded returns true
    abstract fun dispatch(context: CoroutineContext, block: Runnable)

    //because in kotlin abstract classes have complete functions
    override fun <T : Any> interceptContinuation(continuation: Continuation<T>):
            Continuation<T> = DispatchedContinuation(this, continuation)

    public operator fun plus(other: CoroutineDispatcher) = other
    override fun toString(): String = "${javaClass.simpleName}@${javaClass.canonicalName}"

}