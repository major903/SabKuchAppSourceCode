package vedam.subkuch.network.models

import com.google.gson.annotations.SerializedName

data class OMenu(
    val MenuId: Int = 0,
    val CityId: String? = null,
    @SerializedName("Menu")
    var name: String? = null,
    var SortOrder: String? = null,
)