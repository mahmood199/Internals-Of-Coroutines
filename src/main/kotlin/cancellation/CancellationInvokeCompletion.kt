package cancellation

import kotlinx.coroutines.*

fun main() = runBlocking {

    val job = launch(Dispatchers.IO) {
        withTimeout(2000L) {
            println("This is executed before the delay")
            someDelay2()
            println("This is executed after the delay")
        }
    }

    //GOLD line
    job.invokeOnCompletion { cause -> println("We were canceled due to $cause") }

    println("This is executed immediately")
}

suspend fun someDelay2() {
    withContext(Dispatchers.Default) {
        delay(10000L)
    }
}