package cancellation

import kotlinx.coroutines.*

fun main() {
    runBlocking {
        val job = launch(Dispatchers.IO) {
            println("This is executed before the first delay 2")
            stallForTime()
            println("This is executed after the first delay")
        }
        launch(Dispatchers.IO) {
            println("This is executed before the second delay 3")
            job.cancel()
            stallForTime()
            println("This is executed after the second delay 4")
        }
        println("This is executed immediately 1")
    }
}

/**
 * NOTE - the output is expected to be
        This is executed immediately 1
        This is executed before the first delay 2
        This is executed before the second delay 3
        This is executed after the second delay 4

 But the actual output is
        This is executed before the first delay 2
        This is executed immediately 1
        This is executed before the second delay 3
        This is executed after the second delay 4

 Reason -   Therefore, when the println("This is executed immediately 1") statement
            is executed after launching the coroutines, it is immediately printed
            to the console because the main thread is free to execute it. However,
            the first coroutine that prints "This is executed before the first delay 2"
            is running in a background thread and may take some time to execute
            due to the [non-deterministic] nature of coroutines and the Dispatchers.IO dispatcher.


            The non-deterministic nature of coroutines refers to the fact
            that the order of execution of coroutines is not predetermined
            or guaranteed. In other words, the order in which coroutines
            are executed is not fixed, and can vary from run to run or
            even within the same run.

 */



suspend fun stallForTime() {
    withContext(Dispatchers.Default) {
        delay(2000L)
    }
}