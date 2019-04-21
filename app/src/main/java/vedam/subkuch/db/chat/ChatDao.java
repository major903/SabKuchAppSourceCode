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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Chat chat);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Chat chat);

    @Query("SELECT * FROM Chat where (FromProfileId =  :fromId AND ToProfileId = :toId) OR (FromProfileId =  :toId AND ToProfileId = :fromId) ORDER BY TimeStamp DESC")
    LiveData<List<Chat>> getIndividualChat(String fromId, String toId);

    @Query("SELECT * FROM Chat where Uuid = :uuid ORDER BY TimeStamp DESC LIMIT 1")
    Chat getChatByUUID(String uuid);

    @Query("SELECT * FROM Chat where id = :id ORDER BY TimeStamp DESC LIMIT 1")
    Chat getChatById(long id);

    @Query("SELECT * FROM Chat where (FromProfileId = :fromId AND Status = :readStatus) OR (FromProfileId = :toId AND Status != :readStatus)")
    LiveData<List<Chat>> getPendingAckChat(String fromId, String toId, int readStatus);

    @Query("SELECT * FROM Chat where Status = :notSentStatus")
    List<Chat> getPendingChat(int notSentStatus);

    @Query("SELECT COUNT(*) FROM Chat where (ToProfileId = :toId AND Status = :notSentStatus) OR (ToProfileId = :toId AND Status = :SentButNotDeliveredStatus)")
    Integer getTotalUnreadMessagesCount(String toId, int notSentStatus, int SentButNotDeliveredStatus);

    @Query("SELECT * FROM Chat where (FromProfileId = :id) OR (ToProfileId = :id) ORDER BY Timestamp DESC LIMIT 1")
    Chat getLatestChatMessage(String id);

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
