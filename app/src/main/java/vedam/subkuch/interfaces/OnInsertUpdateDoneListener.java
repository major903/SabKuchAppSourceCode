package vedam.subkuch.interfaces;

import vedam.subkuch.db.chat.Chat;

public interface OnInsertUpdateDoneListener {
    void onInsertDone(Chat chat, boolean isOwnMessage);

    void onUpdateDone(int updatedRowsCount, boolean isOwnMessage);
}
