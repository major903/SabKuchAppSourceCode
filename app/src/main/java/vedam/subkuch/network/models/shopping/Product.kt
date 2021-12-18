package vedam.subkuch.network.models.shopping

import com.google.gson.annotations.SerializedName

data class Product(

        @SerializedName("ShoppingId") var ShoppingId: Int? = null,
        @SerializedName("ShoppingSubcatid") var ShoppingSubcatid: Int? = null,
        @SerializedName("BrandName") var BrandName: String? = null,
        @SerializedName("ItemCode") var ItemCode: String? = null,
        @SerializedName("ItemName") var ItemName: String? = null,
        @SerializedName("ItemDescriptionShort") var ItemDescriptionShort: String? = null,
        @SerializedName("ItemDescriptionLong") var ItemDescriptionLong: String? = null,
        @SerializedName("Image1") var Image1: String? = null,
        @SerializedName("Image2") var Image2: String? = null,
        @SerializedName("Image3") var Image3: String? = null,
        @SerializedName("Image4") var Image4: String? = null,
        @SerializedName("Image5") var Image5: String? = null,
        @SerializedName("CityId") var CityId: Int? = null,
        @SerializedName("Isactive") var Isactive: Boolean? = null,
        @SerializedName("CreatedBy") var CreatedBy: String? = null,
        @SerializedName("CreatedOn") var CreatedOn: String? = null

)