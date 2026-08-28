package vedam.subkuch.db.chat;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import vedam.subkuch.interfaces.OnInsertUpdateDoneListener;
import vedam.subkuch.interfaces.RowIdListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatRepository implements RowIdListener {

    private static final ExecutorService DATABASE_EXECUTOR = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private ChatDao chatDao;
    private OnInsertUpdateDoneListener onInsertUpdateDoneListener;

    public ChatRepository(Context context, OnInsertUpdateDoneListener onInsertUpdateDoneListener) {
        ChatRoomDatabase db = ChatRoomDatabase.getDatabase(context);
        chatDao = db.chatDao();
        this.onInsertUpdateDoneListener = onInsertUpdateDoneListener;
    }

    public ChatRepository(Context context) {
        ChatRoomDatabase db = ChatRoomDatabase.getDatabase(context);
        chatDao = db.chatDao();
    }

//    public LiveData<List<Chat>> getIndividualChat(String fromId, String toId) {
//        return chatDao.getIndividualChat(fromId, toId);
//    }

//    public List<Chat> getPendingChat() {
//        return chatDao.getPendingChat(Constants.CHAT_STATUS_NOT_SENT);
//    }
//
//    public LiveData<List<Chat>> getPendingAckChat(String fromId, String toId) {
//        return chatDao.getPendingAckChat(fromId, toId, Constants.CHAT_STATUS_READ);
//    }

//    public Chat getChatByUUID(String UUID) {
//        return chatDao.getChatByUUID(UUID);
//    }

//    public Chat getChatById(long id) {
//        return chatDao.getChatById(id);
//    }

//    public Chat getLatestChatMessage(String id) {
//        return chatDao.getLatestChatMessage(id);
//    }

//    public Integer getTotalUnreadMessagesCount(String id, String chatType) {
//        return chatDao.getTotalUnreadMessagesCount(id, Constants.CHAT_STATUS_NOT_SENT, Constants.CHAT_STATUS_SENT_BUT_NOT_DELIVERED, chatType);
//    }

    public void insert(Chat chat, boolean isOwnMessage) {
        DATABASE_EXECUTOR.execute(() -> {
            long id = chatDao.insert(chat);
            chat.setId(id);
            mainHandler.post(() -> onGetChat(chat, isOwnMessage));
        });
    }

    public void update(Chat chat, boolean isOwnMessage) {
        DATABASE_EXECUTOR.execute(() -> {
            int updatedRowsCount = chatDao.update(chat);
            mainHandler.post(() -> onGetUpdatedRowsCount(updatedRowsCount, isOwnMessage));
        });
    }

    @Override
    public void onGetChat(Chat chat, boolean isOwnMessage) {
        if (onInsertUpdateDoneListener != null)
            onInsertUpdateDoneListener.onInsertDone(chat, isOwnMessage);
    }

    @Override
    public void onGetUpdatedRowsCount(int updatedRowsCount, boolean isOwnMessage) {
        if (onInsertUpdateDoneListener != null)
            onInsertUpdateDoneListener.onUpdateDone(updatedRowsCount, isOwnMessage);
    }

}
