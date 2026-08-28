package vedam.subkuch.network.models.learn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;

import org.junit.Test;

/** Protects the app models against the exact field casing and nesting returned by the live API. */
public class LearnResponseParsingTest {
    private final Gson gson = new Gson();

    @Test
    public void learnHomeParsesCourseAndSubscriptionState() {
        String json = "{\"ReturnCode\":1,\"ReturnMessage\":\"success\",\"ReturnData\":{"
                + "\"Categories\":[{\"CourseCategoryId\":1,\"Name\":\"Artificial Intelligence\"}],"
                + "\"Courses\":[{\"CourseId\":1,\"EncryptedCourseId\":\"encrypted-course-id\",\"CourseCategoryId\":1,\"Name\":\"AI For Dummies\","
                + "\"Price\":499,\"Mrp\":5000,\"IsSubscribed\":false}]}}";

        LearnHomeResponse response = gson.fromJson(json, LearnHomeResponse.class);

        assertEquals("1", response.getReturnCode());
        assertEquals(1, response.getReturnData().getCategories().size());
        LearnCourse course = response.getReturnData().getCourses().get(0);
        assertEquals(1, course.getCourseId());
        assertEquals("encrypted-course-id", course.getEncryptedCourseId());
        assertEquals("AI For Dummies", course.getName());
        assertFalse(course.isSubscribed());
    }

    @Test
    public void courseDetailsParsesChaptersAndPreviewVideo() {
        String json = "{\"ReturnCode\":1,\"ReturnMessage\":\"success\",\"ReturnData\":{"
                + "\"Course\":{\"CourseId\":1,\"Name\":\"AI For Dummies\",\"IsSubscribed\":false},"
                + "\"Chapters\":[{\"ChapterId\":3,\"ChapterName\":\"Introduction\","
                + "\"ChapterNumber\":1,\"Topics\":[{\"TopicId\":4,\"TopicName\":\"Course Content\","
                + "\"TopicNumber\":1,\"PaidOrFree\":false,\"IsLocked\":false,"
                + "\"VideoEmbedUrl\":\"https://player.mediadelivery.net/play/726501/video-id\"}]}],"
                + "\"MyRating\":0}}";

        CourseDetailsResponse response = gson.fromJson(json, CourseDetailsResponse.class);

        assertNotNull(response.getReturnData().getCourse());
        LearnChapter chapter = response.getReturnData().getChapters().get(0);
        assertEquals("Introduction", chapter.getChapterName());
        LearnTopic topic = chapter.getTopics().get(0);
        assertFalse(topic.isLocked());
        assertTrue(topic.getVideoEmbedUrl().startsWith("https://player.mediadelivery.net/play/"));
    }

    @Test
    public void myCoursesParsesPreviouslyEnrolledCourse() {
        String json = "{\"ReturnCode\":1,\"ReturnMessage\":\"success\",\"ReturnData\":[{"
                + "\"CourseId\":7,\"Name\":\"Purchased Course\",\"IsSubscribed\":true}]}";

        MyCoursesResponse response = gson.fromJson(json, MyCoursesResponse.class);

        assertEquals(1, response.getReturnData().size());
        assertTrue(response.getReturnData().get(0).isSubscribed());
    }

    @Test
    public void searchCoursesParsesNestedCourseList() {
        String json = "{\"ReturnCode\":1,\"ReturnData\":{\"Query\":\"work\",\"Courses\":[{"
                + "\"CourseId\":2,\"Name\":\"Work from Home Master Class\"}]}}";

        SearchCoursesResponse response = gson.fromJson(json, SearchCoursesResponse.class);

        assertEquals("work", response.getReturnData().getQuery());
        assertEquals(2, response.getReturnData().getCourses().get(0).getCourseId());
    }

    @Test
    public void coursesByCategoryParsesNestedCourseList() {
        String json = "{\"ReturnCode\":1,\"ReturnData\":{\"Category\":{"
                + "\"CourseCategoryId\":1,\"Name\":\"Artificial Intelligence\"},\"Courses\":[{"
                + "\"CourseId\":1,\"Name\":\"AI For Dummies\"}]}}";

        CoursesByCategoryResponse response = gson.fromJson(json, CoursesByCategoryResponse.class);

        assertEquals(1, response.getReturnData().getCategory().getCourseCategoryId());
        assertEquals("AI For Dummies", response.getReturnData().getCourses().get(0).getName());
    }
}
