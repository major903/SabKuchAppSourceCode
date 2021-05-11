package vedam.subkuch.ui.directory.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    var BusinessReview: String?,
    val Rating: String?,
    val UserName: String?,
) : Parcelable