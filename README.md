# Internals-Of-Suspending-Functions

Hi folks,\
In this repo, we will see how a kotlin coroutine asynchronous call differs from a regular function execution.
This repo also contains
1. How a coroutine works internally.
2. Different classes involved in the construct of a suspending fiunctions and their roles
3. How the bytecode is generated for each suspending functions with the classes we discussed in 2. (Basic and advanced implementation).
4. Classes involved while switching context.
5. How context switching works internally with all these classes.
6. How exception is handled in a coroutine.



# Normal Execution of function

[![Screenshot-2023-01-03-003831.png](https://i.postimg.cc/TYSCcwyz/Screenshot-2023-01-03-003831.png)](https://postimg.cc/Mv776zm5)

# Execution of function when suspend keyword is used

[![Screenshot-2023-01-03-010755.png](https://i.postimg.cc/7Y0MkNtL/Screenshot-2023-01-03-010755.png)](https://postimg.cc/KRcg7t2X)

#Kotlin Continuation Interface.\
This is used under the hood for the asynchronous call back in coroutines which makes coroutines appear to be synchronous\

![Screenshot 2023-01-09 231227](https://user-images.githubusercontent.com/58071934/211372875-77952cdd-c644-407c-b638-ef541b59d533.png)


#Kotlin ContinuationImpl Abstract class.\
This will be implemented by the class representation of the funtion in the byte code. This helps the suspend function for state management. The variable `label` is used for that.\
![Screenshot 2023-01-09 232332](https://user-images.githubusercontent.com/58071934/211375205-0246326c-a27a-4392-b5d2-0ef95bda8ebd.png)


#A demonstration class which shows how a method is implemented under the hood by the Kotlin compiler
![Screenshot 2023-01-10 221756](https://user-images.githubusercontent.com/58071934/211612715-f7a80673-4332-409b-93f9-6f099e204561.png)


#Inheritance structure of the state machine classes.\
![Screenshot 2023-01-11 195128](https://user-images.githubusercontent.com/58071934/211830124-0ed250d1-0adc-42de-b3b2-caa1350bfc32.png)

#NOTE - \
a suspend function will only suspend if the suspending functions it calls returns COROUTINE_SUSPENDED otherwise, it will cast the result of the function to the expected type— Profile and Terms in this case—and continue executing the next label. This guarantees that no unnecessary suspensions occur.


#Coroutine terminology\
1. CoroutineContext
Coroutine are always started in a context which is coroutine context\
CoroutineContext decides the thread on which the execution of a given block of code should happen.\
CoroutineContext helps in switching between threads\
Context switching is very hard in threads. Context switching means changing of threads to execute different portions of the code. \


2. CoroutineScope\
Lifetime of a coroutine. Determines the lifetime/span/boundary to which a coroutine must be alive. Helps in cancellation of coroutine if the scope owner is destroyed\
A coroutine can have child coroutine and the child coroutines are cancelled/ destroyed if the parent coroutine is destroyed.

3. Dispatchers - Basically a thread pool \
Helps to dispatch on which thread the coroutine must run \
Way to define on which thread the coroutine must start. \
Dispatch our coroutine to threads\
Types of dispatchers available \
    a. IO\
    b. Main\
    c. Default\
    d. Unconfined\

\
A coroutine is an instance of suspendable computation. It is conceptually similar to a thread, in the sense that it takes a block of code to run that works concurrently with the rest of the code. However, a coroutine is not bound to any particular thread. It may suspend its execution in one thread and resume in another one.


#Chain of function calls and their classes that are involved forr the creation of a cotoutine
1. 
![Screenshot 2023-01-20 163948](https://user-images.githubusercontent.com/58071934/213681739-25d75835-4ca8-44d2-b1a6-a411e759c6e4.png)



