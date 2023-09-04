package rebuilding_framework.Intro

fun main() {
    fun1 {

    }
}

fun fun1(continuation: () -> Unit) {
    fun2 {
        print("!")
        continuation()
    }
}

fun fun2(continuation: () -> Unit) {
    fun3 {
        print("World")
        continuation()
    }
}

fun fun3(continuation: () -> Unit) {
    print("Hello")
    continuation()
}
