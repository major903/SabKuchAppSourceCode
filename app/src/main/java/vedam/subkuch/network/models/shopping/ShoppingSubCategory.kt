package vedam.subkuch.network.models.shopping

import com.google.gson.annotations.SerializedName

data class ShoppingSubCategory(

        @SerializedName("ShoppingSubcatid") var ShoppingSubcatid: Int? = null,
        @SerializedName("Name") var Name: String? = null,
        @SerializedName("Image") var Image: String? = null

)