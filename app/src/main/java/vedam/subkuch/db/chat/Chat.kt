package vedam.subkuch.db.chat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Chat(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var docId: String? = null,
    var fromProfileId: String? = null,
    var toProfileId: String? = null,
    var message: String? = null,
    var timeStamp: String? = null,
    var senderName: String? = null,
    var isStatus: Boolean = false,
    var isRead: Boolean = false,
    var idPair: String? = null,
) {
//    constructor(chat: Chat) {
//        id = chat.id
//        fromProfileId = chat.fromProfileId
//        toProfileId = chat.toProfileId
//        message = chat.message
//        timeStamp = chat.timeStamp
//        senderName = chat.senderName
//        isStatus = chat.isStatus
//        idPair = chat.idPair
//    }

    constructor() : this(0)
}