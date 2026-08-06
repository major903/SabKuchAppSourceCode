package vedam.subkuch.network.models

data class ContactObject(
    val UserId: Int,
    val Name: String? = null,
    var Mobile1: String? = null,
    var Mobile2: String? = null,
    var Mobile3: String? = null,
    val Status: Boolean = true,
    val CityId: Int = 0,
)
