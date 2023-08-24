import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import java.util.concurrent.Executors

@OptIn(DelicateCoroutinesApi::class)
fun main() {

    //To be deprecated
    val multiThreadedCoroutineDispatcher = newFixedThreadPoolContext(10, "MyThreadPool")
    multiThreadedCoroutineDispatcher.executor.execute {

    }

    //To be deprecated
    val singleThreadedCoroutineDispatcher = newSingleThreadContext("MySingle")
    singleThreadedCoroutineDispatcher.executor.execute {

    }

    //Recommended
    val x = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
    x.executor.execute {

    }

}