package vedam.subkuch.db.chat;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Chat.class}, version = 2)
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
