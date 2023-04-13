package cancellation

import kotlinx.coroutines.*

fun main() = runBlocking {
    val job = launch(Dispatchers.IO) {
        try {
            println("This is executed before the first delay")
            tryDelay()
            println("This is executed after the first delay")
        } finally {
            withContext(NonCancellable) {
                println("This is executed before the finally block delay")
                tryDelay()
                println("This is executed after the finally block delay")
            }
        }
    }

    launch(Dispatchers.IO) {
        println("This is executed at the start of the second coroutine")
        job.cancelAndJoin()
        println("This is executed before the second delay")
        tryDelay()
        println("This is executed after the second delay")
    }

    println("This is executed immediately")
}

suspend fun tryDelay() {
    withContext(Dispatchers.Default) {
        delay(2000L)
    }
}