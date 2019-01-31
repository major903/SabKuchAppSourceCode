package vedam.subkuch.ui.ask.models;

import java.util.ArrayList;

public class Conversation {

    private ArrayList<Reply> blogReplydetails;

    private String Postedon;

    private String Topic;

    private String Replayallowed;

    private String Categoryid;

    private String Blogid;

    private boolean isExpanded;

    private String Username;

    public String getUsername() {
        return Username;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public ArrayList<Reply> getBlogReplydetails() {
        return blogReplydetails;
    }

    public void setBlogReplydetails(ArrayList<Reply> blogReplydetails) {
        this.blogReplydetails = blogReplydetails;
    }

    public String getPostedon() {
        return Postedon;
    }

    public void setPostedon(String Postedon) {
        this.Postedon = Postedon;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String Topic) {
        this.Topic = Topic;
    }

    public String getReplayallowed() {
        return Replayallowed;
    }

    public void setReplayallowed(String Replayallowed) {
        this.Replayallowed = Replayallowed;
    }

    public String getCategoryid() {
        return Categoryid;
    }

    public void setCategoryid(String Categoryid) {
        this.Categoryid = Categoryid;
    }

    public String getBlogid() {
        return Blogid;
    }

    public void setBlogid(String Blogid) {
        this.Blogid = Blogid;
    }
}
