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
This is used under the hood for the asynchronous call back in coroutines which makes coroutines appear to be synchronous

![Screenshot 2023-01-09 231227](https://user-images.githubusercontent.com/58071934/211372875-77952cdd-c644-407c-b638-ef541b59d533.png)
