package cancellation

import kotlinx.coroutines.*

// Custom handling of TimeoutCancellationException

fun main() = runBlocking {
    launch(Dispatchers.IO) {
        try {
            withTimeout(2000L) {
                println("This is executed before the delay")
                timeoutDelay2()
                println("This is executed after the delay")
            }
        } catch (e: TimeoutCancellationException) {
            println("We got a timeout exception $e")
        }
        println("This is printed after the timeout")
    }

    println("This is executed immediately")
}

suspend fun timeoutDelay2() {
    withContext(Dispatchers.Default) {
        delay(10000L)
    }
}

/**
 *      In this case, not only do we see the “We got a timeout exception”
 *      message, but we also see the “This is printed after the timeout”
 *      message. We handled the raised exception, so execution can proceed
 *      normally after our try/catch logic
 */