package vedam.subkuch.network.models.learn;

import java.util.ArrayList;

public class LearnChapter {
    private int ChapterId;
    private String ChapterName;
    private int ChapterNumber;
    private String ChapterDuration;
    private ArrayList<LearnTopic> Topics;

    public int getChapterId() { return ChapterId; }
    public String getChapterName() { return ChapterName; }
    public int getChapterNumber() { return ChapterNumber; }
    public String getChapterDuration() { return ChapterDuration; }
    public ArrayList<LearnTopic> getTopics() { return Topics; }
}
