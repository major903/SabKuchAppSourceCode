package vedam.subkuch.db.chat;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

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

    public LiveData<List<Chat>> getIndividualChat(String fromId, String toId) {
        return chatDao.getIndividualChat(fromId, toId);
    }

    /*public LiveData<List<Chat>> getChatList(String fromId, String toId) {
        return chatDao.getChatList(fromId, toId);
    }*/


    public void insert(Chat chat, boolean isOwnMessage) {
        new insertAsyncTask(chatDao, true, this, isOwnMessage).execute(chat);
    }

    public void update(Chat chat, boolean isOwnMessage) {
        new insertAsyncTask(chatDao, false, this, isOwnMessage).execute(chat);
    }

    @Override
    public void onGetId(long id, boolean isOwnMessage) {
        if (onInsertUpdateDoneListener != null)
            onInsertUpdateDoneListener.onInsertUpdateDone(id, isOwnMessage);
    }

    private static class insertAsyncTask extends AsyncTask<Chat, Long, Long> {

        private ChatDao mAsyncTaskDao;
        private boolean isInsert;
        private RowIdListener rowIdListener;
        private boolean isOwnMessage;

        insertAsyncTask(ChatDao dao, boolean isInsert, RowIdListener rowIdListener, boolean isOwnMessage) {
            mAsyncTaskDao = dao;
            this.isInsert = isInsert;
            this.rowIdListener = rowIdListener;
            this.isOwnMessage = isOwnMessage;
        }

        @Override
        protected Long doInBackground(final Chat... params) {
            if (isInsert)
                return mAsyncTaskDao.insert(params[0]);
            return (long) mAsyncTaskDao.update(params[0]);
        }

        @Override
        protected void onPostExecute(Long id) {
            super.onPostExecute(id);
            if (rowIdListener != null)
                rowIdListener.onGetId(id, isOwnMessage);
        }
    }
}
