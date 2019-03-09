package vedam.subkuch.interfaces;

import vedam.subkuch.db.chat.Chat;

public interface RowIdListener {
    void onGetChat(Chat chat, boolean isOwnMessage);

    void onGetUpdatedRowsCount(int updatedRowsCount, boolean isOwnMessage);
}
