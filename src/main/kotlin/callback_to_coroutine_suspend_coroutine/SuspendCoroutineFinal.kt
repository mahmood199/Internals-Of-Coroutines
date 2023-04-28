package callback_to_coroutine_suspend_coroutine

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


fun main(): Unit = runBlocking {
    launch {
        val tasks = suspendCoroutine { continuation ->
            downloadTasks(ContinuationAdapter(continuation))
        }
        printTasks(tasks)
    }
}

private class ContinuationAdapter(
    private val continuation: Continuation<List<Task>>
) : DownloadCallback {

    override fun onDownloaded(tasks: List<Task>) {
        continuation.resume(tasks)
    }
}