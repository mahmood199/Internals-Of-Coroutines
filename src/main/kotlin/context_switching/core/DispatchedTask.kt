package context_switching.core

import kotlinx.coroutines.Runnable

interface DispatchedTask<in T> : Runnable {
    override fun run() {
        TODO("Not yet implemented")
    }
}