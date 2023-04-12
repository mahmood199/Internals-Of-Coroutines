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
 *      When we cancel the parent, both that job and our withContext()
 *      child job are canceled. When we call delay() in the withContext()
 *      job, the coroutines system sees that our job was canceled, so it
 *      abandons execution, so we never get the “This is executed at the
 *      end of the child job”. Similarly, the coroutines system sees
 *      that the parent job was canceled while it was blocked waiting on
 *      stallForTime() to return,so it abandons execution of that job
 *      too, so we never see the “This is executed after the delay” message.
 */