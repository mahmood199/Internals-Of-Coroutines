package callback_to_coroutine_suspend_coroutine


fun main() {
    downloadTasks(
        object : DownloadCallback {
            override fun onDownloaded(tasks: List<Task>) {
                printTasks(tasks)
            }
        }
    )
}

interface DownloadCallback {
    fun onDownloaded(tasks: List<Task>)
}

fun downloadTasks(callback: DownloadCallback) {
    println("Downloading…")
    // code that makes a network call and returns the list of tasks
    callback.onDownloaded(listOf(Task(1), Task(2), Task(3)))
}

fun printTasks(tasks: List<Task>) {
    tasks.forEach {
        println("Printing ${it.id}")
    }
}
