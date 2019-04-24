package vedam.subkuch.db.chat;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnInsertUpdateDoneListener;
import vedam.subkuch.interfaces.RowIdListener;

public class ChatRepository implements RowIdListener {

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

    public LiveData<List<Chat>> getIndividualChat(String fromId, String toId) {
        return chatDao.getIndividualChat(fromId, toId);
    }

    public List<Chat> getPendingChat() {
        return chatDao.getPendingChat(Constants.CHAT_STATUS_NOT_SENT);
    }

    public LiveData<List<Chat>> getPendingAckChat(String fromId, String toId) {
        return chatDao.getPendingAckChat(fromId, toId, Constants.CHAT_STATUS_READ);
    }

    public Chat getChatByUUID(String UUID) {
        return chatDao.getChatByUUID(UUID);
    }

    public Chat getChatById(long id) {
        return chatDao.getChatById(id);
    }

    public Chat getLatestChatMessage(String id) {
        return chatDao.getLatestChatMessage(id);
    }

    public Integer getTotalUnreadMessagesCount(String id, String chatType) {
        return chatDao.getTotalUnreadMessagesCount(id, Constants.CHAT_STATUS_NOT_SENT, Constants.CHAT_STATUS_SENT_BUT_NOT_DELIVERED, chatType);
    }

    public void insert(Chat chat, boolean isOwnMessage) {
        new InsertAsyncTask(chatDao, this, isOwnMessage).execute(chat);
    }

    public void update(Chat chat, boolean isOwnMessage) {
        new UpdateAsyncTask(chatDao, this, isOwnMessage).execute(chat);
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

    private static class InsertAsyncTask extends AsyncTask<Chat, Void, Chat> {

        private ChatDao mAsyncTaskDao;
        private RowIdListener rowIdListener;
        private boolean isOwnMessage;

        InsertAsyncTask(ChatDao dao, RowIdListener rowIdListener, boolean isOwnMessage) {
            mAsyncTaskDao = dao;
            this.rowIdListener = rowIdListener;
            this.isOwnMessage = isOwnMessage;
        }

        @Override
        protected Chat doInBackground(final Chat... params) {
            Chat chat = params[0];
            long id = mAsyncTaskDao.insert(chat);
            chat.setId(id);
            return chat;
        }

        @Override
        protected void onPostExecute(Chat chat) {
            super.onPostExecute(chat);
            if (rowIdListener != null) {
                rowIdListener.onGetChat(chat, isOwnMessage);
            }
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Chat, Void, Integer> {

        private ChatDao mAsyncTaskDao;
        private RowIdListener rowIdListener;
        private boolean isOwnMessage;

        UpdateAsyncTask(ChatDao dao, RowIdListener rowIdListener, boolean isOwnMessage) {
            mAsyncTaskDao = dao;
            this.rowIdListener = rowIdListener;
            this.isOwnMessage = isOwnMessage;
        }

        @Override
        protected Integer doInBackground(final Chat... params) {
            return mAsyncTaskDao.update(params[0]);
        }

        @Override
        protected void onPostExecute(Integer updatedRowsCount) {
            super.onPostExecute(updatedRowsCount);
            if (rowIdListener != null)
                rowIdListener.onGetUpdatedRowsCount(updatedRowsCount, isOwnMessage);
        }
    }
}
