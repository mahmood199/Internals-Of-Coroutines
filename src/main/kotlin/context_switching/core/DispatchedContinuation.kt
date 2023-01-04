package context_switching.core

import suspension.core.Continuation

class DispatchedContinuation<in T : Any>(
    val dispatcher: CoroutineDispatcher,
    private val continuation: Continuation<T>
) : Continuation<T> by continuation, DispatchedTask<T> {

    companion object {
        //come constant value
        const val DEFAULT = 1
        const val MODE_ATOMIC_DEFAULT = 1
    }

    var _state = Any()
    var resumeMode = DEFAULT
    override fun resume(value: T) {
        val context = continuation.context
        if(dispatcher.isDispatchNeeded(context)){
            _state = value
            resumeMode = MODE_ATOMIC_DEFAULT
            dispatcher.dispatch(context, this)
        } else {
            resumeUndispatched(value)
        }
    }

    private fun resumeUndispatched(value: T) {
    }

    override fun resumeWithException(exception: Throwable) {
        val context = continuation.context
        if(dispatcher.isDispatchNeeded(context)) {
            _state = CompletedExceptionally(exception)
            resumeMode = MODE_ATOMIC_DEFAULT
            dispatcher.dispatch(context, this)
        } else
            resumeUndispatchedWithException(exception)
    }

    private fun resumeUndispatchedWithException(exception: Throwable) {

    }

    override fun run() {
        super.run()
        //some conditions to check for complex operations
        //ie within a coroutineContext block resume the continuation
        val context = continuation.context
        /**
         *
            withCoroutineContext(context) {
                if (job != null && !job.isActive) {
                    continuation.resumeWithException(
                    job.getCancellationException())
                } else {
                val exception = getExceptionalResult(state)
                if (exception != null) {
                    continuation.resumeWithException(exception)
                } else {
                    continuation.resume(getSuccessfulResult(state))
                }
            }
         */
    }

}

class CompletedExceptionally(exception: Throwable) {

}
