package cancellation

import kotlinx.coroutines.*

// Default handling of TimeoutCancellationException

fun main() = runBlocking {
    launch(Dispatchers.IO) {
        withTimeout(2000L) {
            println("This is executed before the delay")
            timeoutDelay()
            println("This is executed after the delay")
        }

        println("This is printed after the timeout")
    }

    println("This is executed immediately")
}

suspend fun timeoutDelay() {
    withContext(Dispatchers.Default) {
        delay(10000L)
    }
}

/**
 *      Here, we time out after two seconds, for a delay() of ten seconds. Ten seconds
 *      exceeds our timeout period, so the job created by withTimeout() is canceled, and we
 *      never see our “This is executed after the delay” message.
 *
 *      We also do not see the “This is printed after the timeout” message, though.
 *
 *      The reason for that is because withTimeout() throws a
 *      TimeoutCancellationException. That exception gets handled inside of launch() by
 *      default.
 *
 *      Since cancellation is considered a normal thing to do, the
 *      TimeoutCancellationException does not trigger a crash.
 *
 *      However, since withTimeout() threw an exception,
 *      the rest of our code inside of launch() gets skipped
 *
 */