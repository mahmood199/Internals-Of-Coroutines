package context_switching.core.dispatchers

/**
*   In practice, the only dispatcher that should override
*   isDispatchNeeded() is Unconfined. All the other dispatchers should
*   always enforce a thread or pool of threads.
*/


/**
 *  The initial Continuation gets wrapped into a DispatchedContinuation; this is still a
    Continuation, but can dispatch using a CoroutineDispatcher.
    The CoroutineDispatcher will use whichever executor fits its requirements,
    sending it a DispatchedTask, which  is a Runnable that sets
    the correct context using withCoroutineContext() and invokes the resume() and
    resumeWithException() functions from the DispatchedContinuation
 */

/**
 *  Continuations are wrapped into DispatchedContinuations at runtime. This
    allows the CoroutineDispatcher to intercept the coroutine, both when started
    and when resumed. At this point, the thread will be enforced—except for
    UNCONFINED
 */

/**
 *  If the CoroutineContext doesn't have a CoroutineExceptionHandler and
    the uncaught exception is not CancellationException, the framework will
    cancel the Job—if any—and allow platform-specific code to handle the
    exception.
 */