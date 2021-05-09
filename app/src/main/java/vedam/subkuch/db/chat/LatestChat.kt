package vedam.subkuch.db.chat

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class LatestChat(
    var latestMessage: String? = null,
    var idPair: String? = null,
    var senderName: String? = null,
    var fromProfileId: String? = null,
    var toProfileId: String? = null,
    var timeStamp: Timestamp? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    var id: String? = null,
) : Parcelable
