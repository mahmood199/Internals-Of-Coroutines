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

# Execution of function when suspend keyword is used (When any non-blocking call is invoked)

[![Screenshot-2023-01-03-010755.png](https://i.postimg.cc/7Y0MkNtL/Screenshot-2023-01-03-010755.png)](https://postimg.cc/KRcg7t2X)

# Kotlin Continuation Interface.
This is used under the hood for the asynchronous call back in coroutines which makes coroutines appear to be synchronous\

![Screenshot 2023-01-09 231227](https://user-images.githubusercontent.com/58071934/211372875-77952cdd-c644-407c-b638-ef541b59d533.png)


# Kotlin ContinuationImpl Abstract class.
This will be implemented by the class representation of the funtion in the byte code. This helps the suspend function for state management. The variable `label` is used for that.\
![Screenshot 2023-01-09 232332](https://user-images.githubusercontent.com/58071934/211375205-0246326c-a27a-4392-b5d2-0ef95bda8ebd.png)


# A demonstration class which shows how a method is implemented under the hood by the Kotlin compiler
![Screenshot 2023-01-10 221756](https://user-images.githubusercontent.com/58071934/211612715-f7a80673-4332-409b-93f9-6f099e204561.png)


# Inheritance structure of the state machine classes.
![Screenshot 2023-01-11 195128](https://user-images.githubusercontent.com/58071934/211830124-0ed250d1-0adc-42de-b3b2-caa1350bfc32.png)

# NOTE - 
a suspend function will only suspend if the suspending functions it calls returns COROUTINE_SUSPENDED otherwise, it will cast the result of the function to the expected type— Profile and Terms in this case—and continue executing the next label. This guarantees that no unnecessary suspensions occur.


# Coroutine terminology
1. CoroutineContext
A CoroutineContext has a key-value store of Element objects, keyed by a
class
Coroutine are always started in a context which is coroutine context\
CoroutineContext decides the thread on which the execution of a given block of code should happen.\
CoroutineContext helps in switching between threads\
Context switching is very hard in threads. Context switching means changing of threads to execute different portions of the code. \


2. CoroutineScope\
Lifetime of a coroutine. Determines the lifetime/span/boundary to which a coroutine must be alive. Helps in cancellation of coroutine if the scope owner is destroyed\
A coroutine can have child coroutine and the child coroutines are cancelled/ destroyed if the parent coroutine is destroyed.

3. Dispatchers - Basically a thread pool. Helps to dispatch on which thread the coroutine must run.
Way to define on which thread the coroutine must start. \
Dispatch our coroutine to threads\
Types of dispatchers available \
    a. IO - On the IO dispatcher there are by default 64 threads, so there could be up to 64 parallel tasks running on that dispatcher.for doing IO bound work\
    b. Main - \
    c. Default - The difference is that Dispatchers.Default is limited to the number of CPU cores (with a minimum of 2) so only N (where N == cpu cores) tasks can run in parallel in this dispatcher. For doing CPU bound work\
    d. Unconfined\
    
use Dispatchers.IO for I/O bound work or other work thattends to block, and use Dispatchers.Default for work that tends not to block.

\
A coroutine is an instance of suspendable computation. It is conceptually similar to a thread, in the sense that it takes a block of code to run that works concurrently with the rest of the code. However, a coroutine is not bound to any particular thread. It may suspend its execution in one thread and resume in another one.


# Chain of function calls and their classes that are involved forr the creation of a coroutine
1. 
![Screenshot 2023-01-20 163948](https://user-images.githubusercontent.com/58071934/213681739-25d75835-4ca8-44d2-b1a6-a411e759c6e4.png)

2. 
![Screenshot 2023-01-20 164241](https://user-images.githubusercontent.com/58071934/213682195-98abd9ce-ebbd-4384-95ce-572136c706f9.png)

3. This diagram shows the heirarchy of the DispatchContinuation class and how it calls the dispatcher() method of the coroutine dispatcher class.
![image](https://user-images.githubusercontent.com/58071934/213872165-7cee3fc6-9414-4963-a301-d468c551c44e.png)


## Coroutine builder
All coroutines start with a coroutine builder. The block of code passed to the builder,
along with anything called from that code block (directly or indirectly), represents the coroutine. They all create a coroutine scope

1. launch - returns a refernce to a Job object.
2. async - returns a deferred object which is a sub-class of Job.
3. runBlocking - 
4. withContext - takes the dispatchers and executes the code supplied as lambda. This also can be considered as a coroutine builder as it creates a new scope just like other. This is basically used to change the dispatchers ie to execute some part of code by other thread pool. 
withContext not only changes context but also create a new job that is child to the parent job.

<br>

|                                                   launch                                                  |                                                                                    async                                                                                    |
|:---------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|                                              Fire and forget                                              |                                                                        Perform task and return result                                                                       |
|                       launch returns a `job` and does not carry any resulting value                       |                              async returns an instance of Deferred<T>, which has an  await() function that returns the result of the coroutine                              |
| If the exception comes inside the launch block, it  will crash the application if we have not handled it. | If exception comes inside the async block, it is stored  inside the deferring result and is not delivered anywhere else,  it will get dropped/ignored unless we handled it. |


<br>

### supervisorScope
The default behavior of a CoroutineScope is if one coroutine fails with an exception,
the scope cancels all coroutines in the scope. Frequently, that is what we want. If we
are doing N coroutines and need the results of all N of them to proceed, as soon as
one crashes, we know that we do not need to waste the time of doing the rest of the
work. However, sometimes that is not what we want. For example, suppose that we are
uploading N images to a server. Just because one image upload fails does not
necessarily mean that we want to abandon uploading the remaining images. Instead,
we might want to complete the rest of the uploads, then find out about the failures
and handle them in some way (e.g., retry policy). \

NOTE - A coroutine is not bound to be executed by a thread. It can be started, paused in one thread and resumed on another thread when it gets free from suspending work. Similarly a thread is not bound to run a single coroutine. It can run multiple coroutines. 
However there is a way to retrict the coroutine to single thread ie by using a single custom thread dispatcher.

## await() on async, join() on job are blocking calls. 
## join() -> no action if completed. 
## await -> Throws if completed. 

