package suspension.implementation

import models.Profile
import models.Terms
import models.UserSummary
import sun.rmi.runtime.Log
import suspension.core.Continuation
import suspension.core.ContinuationImpl
import kotlin.coroutines.CoroutineContext

class GetUserSummarySM : ContinuationImpl<UserSummary?>() {

    //Coroutine Impl values holder
    // result holder
    var value: UserSummary? = null
    var exception: Throwable? = null
    //to store the initial continuation that is sent when the execution
    //of getUserSummary() is first started
    var cont: Continuation<UserSummary?>? = null

    //Function variables
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

    //We check whether cont is an instance of GetUserSummarySM; if that's the case,
    //we use it as the state. If not, that means that it's the initial execution of the
    //function, so a new one is created.
    val sm = cont as? GetUserSummarySM ?: GetUserSummarySM()

    when(sm.label) {
        0 -> {
            // Label 0 -> first execution
            //First time assign continuation
            // then assign value of returned
            // variable after casting
            throwOnFailure(sm.exception)
            sm.cont = cont
            Log.getLog("dsa","fetching summary of $id", 1)
            sm.label = 1
            fetchProfile(id, sm) // suspending fun
            return
        }
        1 -> {  // label 1 -> resuming
            throwOnFailure(sm.exception)
            sm.profile = sm.value as Profile
            sm.age = calculateAgeCon(sm.profile!!.dateOfBirth)
            sm.label = 2
            sm.terms = sm.value as Terms
            validateTerms(sm.profile!!.country, sm.age!!, sm) // suspending fun
            return
        }
        2 -> { // label 2 -> resuming and terminating
            throwOnFailure(sm.exception)
            sm.label = 3// not required
            sm.terms = sm.value as Terms

            /**Dont return the result. Instead invoke the callback received from cont object
             *
             */
            sm.cont!!.resume(UserSummary(sm.profile!!, sm.age!!, sm.terms!!))
            /*
                        return UserSummary(profile, age, terms)
            */
        } else -> {
            throw IllegalStateException()
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