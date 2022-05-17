package vedam.subkuch.network.models

data class ContactObject(
    val Userid: Long? = 0,
    val Name: String? = null,
    var Mobile1: String? = null,
    var Mobile2: String? = null,
    var Mobile3: String? = null,
    val Status: Boolean? = null,
    val CityId: String? = null,
)
