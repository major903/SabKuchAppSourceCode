package vedam.subkuch.network.models

import com.google.gson.annotations.SerializedName

/** Paginated payload returned by GET /api/Menu/GetMenus. */
data class MenuPage(
    @SerializedName("TotalCount")
    val totalCount: Int = 0,
    @SerializedName("PageIndex")
    val pageIndex: Int = 0,
    @SerializedName("PageSize")
    val pageSize: Int = 0,
    @SerializedName("Menus")
    val menus: ArrayList<OMenu> = arrayListOf(),
)
