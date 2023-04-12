package cancellation

import kotlinx.coroutines.*

fun main() = runBlocking {
    launch(Dispatchers.IO) {
        val result = withTimeoutOrNull(2000L) {
            println("This is executed before the delay")
            someDelay()
            println("This is executed after the delay")
            "hi!"
        }

        println("This is the result: $result")
    }

    println("This is executed immediately")
}

suspend fun someDelay() {
    withContext(Dispatchers.Default) {
        delay(1000L)
    }
}

/**
 *      in this case, since our work exceeds our timeout period, we get a null result.
 *      If you raise the timeout limit or reduce the duration of the delay() call,
 *      such that the work can complete before the timeout period elapses,
 *      you will get "hi" as the result
 */