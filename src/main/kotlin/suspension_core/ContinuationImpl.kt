package suspension_core

abstract class ContinuationImpl<T> : Continuation<T>{
    companion object {
        const val LABEL_INITIAL_VALUE = 0
    }
    var label : Int = LABEL_INITIAL_VALUE
    abstract fun doResume(data: T, exception: Throwable?)
}