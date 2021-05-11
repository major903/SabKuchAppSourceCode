package vedam.subkuch.ui.directory.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BusinessAddress (
    var DealingIn: String? = null,
    var InfoLine1: String? = null,
    var ContactPerson: String? = null,
    var Email: String? = null,
    var Address: String? = null,
    var Mobile1: String? = null,
    var Mobile2: String? = null,
    var longitude: String? = null,
    var latitude: String? = null,
    var Zipcode: String? = null,
    var InfoLine2: String? = null,
    var PhoneNo: String? = null,
    var City: String? = null,
    var Distance: String? = null,
): Parcelable