package vedam.subkuch.network.models.shopping

import com.google.gson.annotations.SerializedName

data class BaseShoppingResponse<T>(

        @SerializedName("status") var status: Boolean? = null,
        @SerializedName("message") var message: String? = null,
        @SerializedName("JobCategoriesResult") var result: JobCategoriesResult<T>? = null

)

data class JobCategoriesResult<T>(

        @SerializedName("ShoppingCategories") var list: List<T>? = null

)