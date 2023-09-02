package rebuilding_framework.advance

import kotlinx.coroutines.delay
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.createCoroutineUnintercepted
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

fun main() {
    // Define a suspend lambda
    val suspendingLambda: suspend () -> Double = suspend {
        calculateFourthRoot(16.0)
    }

    // Define a callback object
    val completionCallback = object : Continuation<Double> {
        override val context: CoroutineContext = EmptyCoroutineContext
        override fun resumeWith(result: Result<Double>) {
            // Prints ④ 2.0
            println("④ ${result.getOrNull()}")
        }
    }

    // Create the coroutine
    val continuation = suspendingLambda.createCoroutineUnintercepted(completionCallback)

    // Start the coroutine
    continuation.resumeWith(Result.success(Unit))
}

// The fourth root is the square root of a square root
suspend fun calculateFourthRoot(number: Double): Double {
    var current = number
    delay(3000)
    println("① $current") // Prints ① 16.0
    current = calculateSquareRoot(current)
    delay(3000)
    println("② $current") // Prints ② 4.0
    current = calculateSquareRoot(current)
    println("③ $current") // Prints ③ 2.0
    return current
}

suspend fun calculateSquareRoot(number: Double): Double {
    val block: (Continuation<Double>) -> Any? = {
        // Do heavy calculation in background thread
//        thread {
            val sqrt = kotlin.math.sqrt(number)
            it.resumeWith(Result.success(sqrt))
//        }
        COROUTINE_SUSPENDED
    }
    return suspendCoroutineUninterceptedOrReturn(block)
}