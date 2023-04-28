package callback_to_coroutine_suspend_coroutine

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    launch {
        print("1 ")
        print("2 ")
        print("3 ")
        print("4 ")
        println("Done!")
    }
}

/**
 *      simply prints 1 2 3 4 Done!.
 */