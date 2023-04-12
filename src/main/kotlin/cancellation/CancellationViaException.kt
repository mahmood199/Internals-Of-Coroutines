package cancellation

import kotlinx.coroutines.*

fun main() = runBlocking {
    val job = launch(Dispatchers.IO) {
        println("This is executed before the first delay")
        someDelayedFunction()
        println("This is executed after the first delay")
    }

    launch(Dispatchers.IO) {
        println("This is executed before the second delay")

        var thisIsReallyNull: String? = null

        println("This will result in a NullPointerException: ${thisIsReallyNull!!.length}")

        someDelayedFunction()
        println("This is executed after the second delay")
    }

    println("This is executed immediately")
}

suspend fun someDelayedFunction() {
    withContext(Dispatchers.Default) {
        delay(2000L)
    }
}

/**
 *    Here, we have two independent jobs. The second job generates a
 *    NullPointerException, so it will be canceled at that point, skipping the rest of its
 *    work. The first job, though, is unaffected, since it is not related to the second job.
 */