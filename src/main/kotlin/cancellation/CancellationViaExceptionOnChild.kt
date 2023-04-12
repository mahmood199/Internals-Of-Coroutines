package cancellation

import kotlinx.coroutines.*

fun main() = runBlocking {
    val job = launch(Dispatchers.IO) {
        println("This is executed before the delay")
        childJobCancellationSample()
        println("This is executed after the delay")
    }

    println("This is executed immediately")
}

suspend fun childJobCancellationSample() {
    withContext(Dispatchers.Default) {
        println("This is executed at the start of the child job")

        var thisIsReallyNull: String? = null

        println("This will result in a NullPointerException: ${thisIsReallyNull!!.length}")

        delay(2000L)
        println("This is executed at the end of the child job")
    }
}