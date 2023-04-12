package cancellation

import kotlinx.coroutines.*

fun main() {
    runBlocking {
        val job = launch(Dispatchers.IO) {
            println("This is executed before the first delay 2")
            stallForTime2()
            println("This is executed after the first delay")
        }
        launch(Dispatchers.IO) {
            println("This is executed before the second delay 3")
            job.cancelAndJoin()
            stallForTime2()
            println("This is executed after the second delay 4")
        }
        println("This is executed immediately 1")
    }
}

suspend fun stallForTime2() {
    withContext(Dispatchers.Default) {
        delay(2000L)
    }
}

/**
 *      Pretty much same as cancel()
 *
 *
 *      BUT!!!!
 *
 *
 *      cancel() is non-blocking call.
 *      Next thing will be executed.
 *      But cancelAndJoin() is a blocking call.
 *
 *
 */