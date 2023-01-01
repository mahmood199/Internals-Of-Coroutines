import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import models.Profile
import models.Terms
import models.UserSummary

fun main(args: Array<String>) = runBlocking {
    println("Hello World!")
    println(getUserSummary(24))
    println("Program arguments: ${args.joinToString()}")
}

suspend fun getUserSummary(id: Int): UserSummary {
    println("fetching summary of $id")
    val profile = fetchProfile(id) // suspending fun
    val age = calculateAge(profile.dateOfBirth)
    val terms = validateTerms(profile.country, age) // suspending fun
    return UserSummary(profile, age, terms)
}

suspend fun fetchProfile(dateOfBirth: Int): Profile {
    delay(500)
    return Profile(dateOfBirth.toString(), "Argentina")
}

suspend fun calculateAge(dateOfBirth: String): Long {
    delay(500)
    return dateOfBirth.toLong()
}

suspend fun validateTerms(country: String, age: Long): Terms {
    delay(500)
    if(country == "Argentina" && age > 23)
        return Terms()
    return Terms()
}
