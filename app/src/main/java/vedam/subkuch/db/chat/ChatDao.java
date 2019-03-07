package vedam.subkuch.db.chat;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ChatDao {

    @Insert
    long insert(Chat chat);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    int update(Chat chat);

    @Query("SELECT * FROM Chat where FromProfileId =  :fromId AND ToProfileId = :toId ORDER BY TimeStamp ASC")
    LiveData<List<Chat>> getIndividualChat(String fromId, String toId);

//    @Query("SELECT * FROM Chat GROUP BY senderName ORDER BY TimeStamp DESC")
//    LiveData<List<Chat>> getChatList(String fromId, String toId);

   /* @Query("SELECT * FROM ChatEntity")
    List<ChatEntity> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);*/
}
