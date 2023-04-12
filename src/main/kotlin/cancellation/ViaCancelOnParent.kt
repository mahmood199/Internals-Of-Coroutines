package cancellation

import kotlinx.coroutines.*

const val CANCEL = 1
const val CANCEL_AND_JOIN = 2

fun main() {
    runBlocking {
        val job = launch(Dispatchers.IO) {
            println("This is executed before the delay")
            coroutineContext[Job]?.let {
                cancelParentJob(it, CANCEL)
                //cancelParentJob(it, CANCEL_AND_JOIN)
            }
            println("This is executed after the delay")
        }
        println("This is executed immediately")
    }
}

suspend fun cancelParentJob(job: Job, cancellationType: Int) {
    withContext(Dispatchers.Default) {
        println("This is executed at the start of the child job")
        when(cancellationType) {
            CANCEL -> job.cancel()
            CANCEL_AND_JOIN -> job.cancelAndJoin()
        }
        println("This is executed after canceling the parent")
        delay(2000L)
        println("This is executed at the end of the child job")
    }
}
/**
 *      The cancel function cancels a coroutine
 *      immediately without waiting for it to
 *      complete, and returns a Boolean indicating
 *      whether the cancellation was successful
 *      or not. It does not wait for the coroutine
 *      to finish and may leave resources and
 *      tasks running in the background.
 *      This function is typically used when
 *      the coroutine needs to be cancelled immediately.
 *
 *      On the other hand, the cancelAndJoin
 *      function cancels a coroutine and waits
 *      for it to complete before returning.
 *      It also rethrows any exceptions that were
 *      thrown by the cancelled coroutine during
 *      its execution. This function is typically
 *      used when the calling coroutine needs to
 *      wait for the cancelled coroutine to complete
 *      and handle any exceptions that occurred during its execution.
 *
 */