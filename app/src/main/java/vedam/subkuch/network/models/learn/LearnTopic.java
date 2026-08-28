package vedam.subkuch.network.models.learn;

public class LearnTopic {
    private int TopicId;
    private String TopicName;
    private int TopicNumber;
    private String TopicDuration;
    private boolean PaidOrFree;
    private boolean IsLocked;
    private String VideoEmbedUrl;

    public int getTopicId() { return TopicId; }
    public String getTopicName() { return TopicName; }
    public int getTopicNumber() { return TopicNumber; }
    public String getTopicDuration() { return TopicDuration; }
    public boolean isPaidOrFree() { return PaidOrFree; }
    public boolean isLocked() { return IsLocked; }
    public String getVideoEmbedUrl() { return VideoEmbedUrl; }
}
