package suspension.implementation

import models.Profile
import models.Terms
import models.UserSummary
import suspension.core.Continuation
import suspension.core.ContinuationImpl
import kotlin.coroutines.CoroutineContext

val COROUTINE_SUSPENDED: Any? = ""

class GetUserSummarySM : ContinuationImpl<UserSummary?>() {

    //Coroutine Impl values holder
    // result holder
    var value: UserSummary? = null
    //To throw exception in case of error
    var exception: Throwable? = null
    //to store the initial continuation that is sent when the execution
    //of getUserSummary() is first started
    var cont: Continuation<UserSummary?>? = null

    /**Function variables
    * Keep all variables as nullable as
    * coroutine can fail at any time and not give us the result.
    */
    val id: Int? = null
    var profile: Profile? = null
    var age: Long? = null
    var terms: Terms? = null

    override fun doResume(data: UserSummary?, exception: Throwable?) {
        this.value = data
        this.exception = exception
        getUserSummary(id!!, this)
    }

    override fun resume(value: UserSummary?) {
        TODO("Not yet implemented")
    }

    override val context: CoroutineContext
        get() = TODO("Not yet implemented")

    override fun resumeWithException(exception: Throwable) {
        TODO("Not yet implemented")
    }

}

fun getUserSummary(
    id: Int,
    cont: Continuation<UserSummary?>
){

    /**We check whether cont is an instance of GetUserSummarySM; if that's the case,
    *we use it as the state. If not, that means that it's the initial execution of the
    *function, so a new one is created.
    */
    val sm = cont as? GetUserSummarySM ?: GetUserSummarySM()

    when(sm.label) {
        0 -> {
            /**Label 0 -> first execution
            * First time assign continuation
            * then assign value of returned
            * variable after casting
            */
            throwOnFailure(sm.exception)
            sm.cont = cont
            print("dsa fetching summary of $id 1")
            sm.label = 1
            val result = fetchProfile(id, sm) // suspending fun
            if (result == COROUTINE_SUSPENDED) return
        }
        1 -> {
            /** label 1 -> resuming
             */
            throwOnFailure(sm.exception)
            sm.profile = sm.value as Profile
            sm.age = calculateAgeCon(sm.profile!!.dateOfBirth)
            sm.label = 2
            sm.terms = sm.value as Terms
            val result = validateTerms(sm.profile!!.country, sm.age!!, sm) // suspending fun
            if (result == COROUTINE_SUSPENDED) return
        }
        2 -> {
            /** label 2 -> resuming and terminating
             */
            throwOnFailure(sm.exception)
            sm.label = -1 // No valid states beyond this
            sm.terms = sm.value as Terms

            /**Dont return the result. Instead invoke the callback received from cont object
             *
             */
            sm.cont!!.resume(UserSummary(sm.profile!!, sm.age!!, sm.terms!!))
        } else -> {
            throw IllegalStateException("call to 'resume' before 'invoke' with coroutine")
        }
    }
}

fun throwOnFailure(result: Throwable?) {
    result?.printStackTrace()
}

fun fetchProfile(id: Int, sm: ContinuationImpl<UserSummary?>): Profile {
    return Profile("13042000", "India")
}

fun validateTerms(country: String, age: Long, sm: ContinuationImpl<UserSummary?>): Boolean {
    return country == "India" && age > 20
}

fun calculateAgeCon(dateOfBirth : String) : Long {
    return 2
}

/**
 * If the suspend function doesn;t return COROUTINE_SUSPENDED to the caller function
 * then the execution of that suspend function happens without suspension. and the
 * result returned by it is casted to the type which is specified in the
 * function declaration
 */