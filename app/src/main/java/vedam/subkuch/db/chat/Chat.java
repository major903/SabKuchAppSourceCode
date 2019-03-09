package vedam.subkuch.db.chat;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Chat {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String SocketType;
    private String FromProfileId;
    private String ToProfileId;
    private String Message;
    private String Uuid;
    private String TimeStamp;
    private String senderName;
    private int Status;

    public Chat(Chat chat) {
        this.id = chat.id;
        this.SocketType = chat.SocketType;
        this.FromProfileId = chat.FromProfileId;
        this.ToProfileId = chat.ToProfileId;
        this.Message = chat.Message;
        this.Uuid = chat.Uuid;
        this.TimeStamp = chat.TimeStamp;
        this.senderName = chat.senderName;
        this.Status = chat.Status;

    }

    public Chat() {
    }

    public String getFromProfileId() {
        return FromProfileId;
    }

    public void setFromProfileId(String fromProfileId) {
        this.FromProfileId = fromProfileId;
    }

    public String getToProfileId() {
        return ToProfileId;
    }

    public void setToProfileId(String toProfileId) {
        this.ToProfileId = toProfileId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String uuid) {
        this.Uuid = uuid;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.TimeStamp = timeStamp;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSocketType() {
        return SocketType;
    }

    public void setSocketType(String socketType) {
        SocketType = socketType;
    }
}
