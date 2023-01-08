package suspension.implementation

import models.Profile
import models.Terms
import models.UserSummary
import suspension.core.Continuation
import suspension.core.ContinuationImpl
import kotlin.coroutines.CoroutineContext

class Basic {

    suspend fun getUserSummary(id: Int): UserSummary {
        println("fetching summary of $id")
        val profile = fetchProfile(id) // suspending fun
        val age = calculateAge(profile.dateOfBirth)
        val terms = validateTerms(profile.country, age) // suspending fun
        return UserSummary(profile, age, terms)
    }

    private fun fetchProfile(dateOfBirth: Int): Profile {
        return Profile(dateOfBirth.toString(), "Argentina")
    }

    private fun calculateAge(dateOfBirth: String): Long {
        return dateOfBirth.toLong()
    }

    private fun validateTerms(country: String, age: Long): Terms {
        if(country == "Argentina" && age > 23)
            return Terms()
        return Terms()
    }

    /**Basic Implementation of internal working of coroutine
    * 1. Divide the function at the points where it can be resumed. After every suspend function call
    * 2. Use label with when statement to decide which part of code to execute
    * 3. Create a state machine which extends ContinuationImpl
    * 4. In the doResume of ContinuationImpl call the same function again, including a continuation params
    * 5. This simple implementation just demonstrates the state machine implementation.
    * 6. It does not takes into consideration the processing of result returned by other suspending functions.
    */

    fun getUserSummaryExplanation(id: Int, continuation: Continuation<String>): UserSummary {

        val sm = object : ContinuationImpl<String>() {
            override fun doResume(data: String, exception: Throwable?) {
                getUserSummaryExplanation(id, this)
            }

            override val context: CoroutineContext
                get() = TODO("Not yet implemented")

            override fun resumeWithException(exception: Throwable) {
                TODO("Not yet implemented")
            }

            override fun resume(value: String) {
                TODO("Not yet implemented")
            }

        }

        val stateMachine = sm as ContinuationImpl<*>
        when(stateMachine.label) {
            0 -> {
                println("fetching summary of $id")
                fetchProfile(id) // suspending fun
            }
            1 -> {
                val age = calculateAge(profile.dateOfBirth)
                val terms = validateTerms(profile.country, age) // suspending fun
            }
            2 -> {
                return UserSummary(profile, age, terms)
            }
        }


    }


}