package context_switching.core.dispatchers

import context_switching.core.CoroutineDispatcher
import java.util.concurrent.RejectedExecutionException
import kotlin.coroutines.CoroutineContext

class CommonPool : CoroutineDispatcher() {

    // Not overriding isDispatchNeeded so it will always return true in this dispatcher.
    // which means that thread/context switching will happen always when continuation is resumed.
    // and the Runnable block will be executed by the threads in the pool

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        try {
    //        (pool ?: getOrCreatePoolSync())
    //            .execute(timeSource.trackTask(block))
        } catch (exception : RejectedExecutionException) {
            exception.printStackTrace()
        }
    }
}