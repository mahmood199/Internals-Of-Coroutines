package callback_to_coroutine_suspend_coroutine_cancellable

import callback_to_coroutine_suspend_coroutine.Task
import kotlinx.coroutines.*
import java.lang.Thread.sleep
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

fun main(): Unit = runBlocking {
    val job = launch(Dispatchers.IO) {
        val tasks = downloadAllTasks()
        printTasks(tasks)
    }

    delay(100)
    job.cancel()
}

private suspend fun downloadAllTasks(): List<Task> {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            print("Cancelled…")
        }
        downloadTasks(ContinuationAdapter(continuation))
    }
}

private class ContinuationAdapter(
    private val continuation: Continuation<List<Task>>
) : DownloadCallback {

    override fun onDownloaded(tasks: List<Task>) {
        continuation.resume(tasks)
    }
}

interface DownloadCallback {
    fun onDownloaded(tasks: List<Task>)
}

private fun downloadTasks(callback: DownloadCallback) {
    println("Downloading…")
    sleep(150) // simulate network latency
    val tasks = listOf(Task(1), Task(2), Task(3))
    callback.onDownloaded(tasks)
}

private fun printTasks(tasks: List<Task>) {
    tasks.forEach { task -> println(task.id) }
}