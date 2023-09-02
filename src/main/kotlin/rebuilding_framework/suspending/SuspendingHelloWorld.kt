package rebuilding_framework.suspending

import kotlinx.coroutines.runBlocking
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.intrinsics.createCoroutineUnintercepted

fun main()= runBlocking {

    val suspendingLambda = suspend {
        "Hello World!"
    }

    val completionCallback = object : Continuation<String> {

        override val context: CoroutineContext = EmptyCoroutineContext

        override fun resumeWith(result: Result<String>) {
            println(result.getOrNull())
        }

    }

    val continuation = suspendingLambda.createCoroutineUnintercepted(completionCallback)

    continuation.resumeWith(Result.success(Unit))

}