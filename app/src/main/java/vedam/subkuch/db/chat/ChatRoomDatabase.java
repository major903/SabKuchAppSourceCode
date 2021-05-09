package vedam.subkuch.db.chat;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Chat.class}, version = 6)
public abstract class ChatRoomDatabase extends RoomDatabase {

    public abstract ChatDao chatDao();

    private static volatile ChatRoomDatabase INSTANCE;

    static ChatRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ChatRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ChatRoomDatabase.class, "chat_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
