package callback_to_coroutine_suspend_coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    launch {
        val tasks = downloadTasks()
        printTasks(tasks)
    }
}

private suspend fun downloadTasks(): List<Task> {
    println("Downloading…")
    delay(1000)
    // code that makes a network call and returns the list of tasks
    return listOf(Task(1), Task(2), Task(3))
}