package vedam.subkuch.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vedam.subkuch.db.chat.Chat

class ChatViewModel : ViewModel() {

    private val _chat = MutableLiveData<Chat?>()
    val chat: LiveData<Chat?> get() = _chat

    fun setLatestChatMessage(chat: Chat?) {
        _chat.postValue(chat)
    }
}