package vedam.subkuch.ui.directory.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Business(
    val Image: String? = null,
    val AvegrageOfRating: String? = null,
    val Addresses: ArrayList<BusinessAddress>? = ArrayList(),
    val Reviews: ArrayList<Review>? = ArrayList(),
    val BusinessID: String? = null,
    val BusinessName: String? = null,
    val Website: String? = null,
    val Country: String? = null,
    val City: String? = null,
) : Parcelable