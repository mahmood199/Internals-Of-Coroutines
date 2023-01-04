package context_switching.core.dispatchers

import context_switching.core.CoroutineDispatcher
import java.lang.UnsupportedOperationException
import kotlin.coroutines.CoroutineContext

class Unconfined : CoroutineDispatcher() {

    // overriding isDispatchNeeded so context switching won't happen. Everything will be done on the same thread / context

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return false
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        throw UnsupportedOperationException()
    }

}