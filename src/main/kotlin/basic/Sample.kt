package basic

import kotlinx.coroutines.*

fun main() {
    runBlocking {
        launch(Dispatchers.IO) {
            val job = launch(Dispatchers.Default) {
                delay(2000L)
                println("This is executed after the delay")
            }
            println("This is executed after calling launch()")
            job.join()
            println("This is executed after join()")
        }
        println("This is executed immediately")
    }
}