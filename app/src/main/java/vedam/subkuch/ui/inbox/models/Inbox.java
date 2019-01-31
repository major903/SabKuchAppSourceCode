package vedam.subkuch.ui.inbox.models;

public class Inbox {

    private String InboxId;

    private String Message;

    private String CreatedAt;

    private String Title;

    private String UserProfileId;

    public String getInboxId() {
        return InboxId;
    }

    public void setInboxId(String InboxId) {
        this.InboxId = InboxId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String CreatedAt) {
        this.CreatedAt = CreatedAt;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getUserProfileId() {
        return UserProfileId;
    }

    public void setUserProfileId(String UserProfileId) {
        this.UserProfileId = UserProfileId;
    }
}
