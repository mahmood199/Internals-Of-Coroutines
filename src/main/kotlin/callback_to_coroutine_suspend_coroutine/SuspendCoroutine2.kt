package callback_to_coroutine_suspend_coroutine

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.suspendCoroutine

fun main(): Unit = runBlocking {
    launch {
        print("1 ")
        print("2 ")
        print("3 ")
        suspendCoroutine<Unit> {  }
        print("4 ")
        println("Done!")
    }
}

/**
 *      it will print 1 2 3 and then it will just wait.
 *      We suspended the coroutine but we did not resume it
 */