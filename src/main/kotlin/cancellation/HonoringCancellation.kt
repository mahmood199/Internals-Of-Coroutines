package cancellation

import kotlinx.coroutines.*
import java.util.*

fun main() = runBlocking {
    val job = launch(Dispatchers.IO) {
        println("This is executed before the first delay")
        stallForCancellation()
        println("This is executed after the first delay")
    }

    println("This is executed immediately")
}

//  First try output with this
suspend fun busyWait(ms: Int) {
    val start = Date().time

    while ((Date().time - start) < ms) {
        // busy loop
    }
}

//  Then with this
suspend fun busyWaitImprovisedV1(ms: Int) {
    val start = Date().time

    while ((Date().time - start) < ms) {
        yield()
    }
}

suspend fun stallForCancellation() {
    withContext(Dispatchers.Default) {
        coroutineContext[Job]!!.cancel()
        println("This is executed before the busyWait(2000) call")
        busyWaitImprovisedV1(2000)
        println("This is executed after the busyWait(2000) call")
    }
}

/**
 *      yield() does two things:
 *      1. Since it is a suspend function, it signals to the coroutines system that it is
 *      safe to switch to another coroutine from this dispatcher, if there is one
 *      around that is ready to run
 *      2. It throws CancellationException if the job was canceled
 */

/**
 *      In our case, we only have this one coroutine, so the first feature is unused. However
 *      it also means that busyWait() will throw CancellationException as soon as the job
 *      is canceled. And, since the job was canceled before the call to busyWait(),
 *      busyWait() will go one pass through the while loop, then throw
 *      CancellationException, courtesy of the yield() call.
 */