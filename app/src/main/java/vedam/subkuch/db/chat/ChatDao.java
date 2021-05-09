package vedam.subkuch.db.chat;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Chat chat);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Chat chat);

    @Query("SELECT * FROM Chat where (fromProfileId =  :fromId AND toProfileId = :toId) OR (fromProfileId =  :toId AND toProfileId = :fromId) ORDER BY timeStamp DESC")
    LiveData<List<Chat>> getIndividualChat(String fromId, String toId);

//    @Query("SELECT * FROM Chat where Uuid = :uuid ORDER BY TimeStamp DESC LIMIT 1")
//    Chat getChatByUUID(String uuid);

    @Query("SELECT * FROM Chat where id = :id ORDER BY timeStamp DESC LIMIT 1")
    Chat getChatById(long id);

//    @Query("SELECT * FROM Chat where (fromProfileId = :fromId AND status = :readStatus) OR (fromProfileId = :toId AND status != :readStatus)")
//    LiveData<List<Chat>> getPendingAckChat(String fromId, String toId, int readStatus);
//
//    @Query("SELECT * FROM Chat where status = :notSentStatus")
//    List<Chat> getPendingChat(int notSentStatus);

//    @Query("SELECT COUNT(*) FROM Chat where (ToProfileId = :toId AND Status = :notSentStatus) AND (ToProfileId = :toId OR Status = :SentButNotDeliveredStatus) AND ChatType = :chatType")
//    Integer getTotalUnreadMessagesCount(String toId, int notSentStatus, int SentButNotDeliveredStatus, String chatType);

    @Query("SELECT * FROM Chat where (fromProfileId = :id) OR (toProfileId = :id) ORDER BY timeStamp DESC LIMIT 1")
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
