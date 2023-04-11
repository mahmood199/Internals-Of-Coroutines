package basic

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

suspend fun main() {
    val x = GlobalScope.async {
        delay(3000L)
        "1223"
    }
    println(x.await())
}

