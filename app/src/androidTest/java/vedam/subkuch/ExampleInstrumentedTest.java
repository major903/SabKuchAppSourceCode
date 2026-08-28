package vedam.subkuch;

import android.app.Activity;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import vedam.subkuch.network.models.OtpResponse;
import vedam.subkuch.ui.jobs.models.City;
import vedam.subkuch.ui.profile.RegisterUserActivity;
import vedam.subkuch.ui.profile.VerificationActivity;
import vedam.subkuch.ui.profile.VerificationIdlingResource;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.runner.lifecycle.Stage.RESUMED;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private String FIRST_NAME = "Nadeem";
    private String LAST_NAME = "Ansari";
    private String MOBILE = "8874055575";
    private String EMAIL = "abc@abc.com";


    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {@link androidx.test.rule.ActivityTestRule}.
     */
    @Rule
    public ActivityScenarioRule<RegisterUserActivity> activityScenarioRule
            = new ActivityScenarioRule<>(RegisterUserActivity.class);
    private VerificationIdlingResource idlingResource;
    private Activity currentActivity;

    /**
     * Use {@link ActivityScenario} to launch and get access to the activity.
     * {@link ActivityScenario#onActivity(ActivityScenario.ActivityAction)} provides a thread-safe
     * mechanism to access the activity.
     */
    public void registerIdlingResource() {
        currentActivity = getActivityInstance();
//        if (currentActivity instanceof VerificationActivity) {
        idlingResource = ((VerificationActivity) currentActivity).getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
//        }

        /*ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                mIdlingResource = activity.getIdlingResource();
                // To prove that the test fails, omit this call:
                IdlingRegistry.getInstance().register(mIdlingResource);
            }
        });*/
    }
    @Test
    public void useAppContext() {

        // Type text and then press the button.
        onView(withId(R.id.et_first_name))
                .perform(typeText(FIRST_NAME));
        onView(withId(R.id.et_last_name))
                .perform(typeText(LAST_NAME));
        onView(withId(R.id.et_mobile_number))
                .perform(scrollTo(), typeText(MOBILE));
        onView(withId(R.id.et_email))
                .perform(scrollTo(), typeText(String.valueOf(EMAIL)), closeSoftKeyboard());
        onView(withId(R.id.et_dob))
                .perform(click());
        onView(withText("OK")).perform(scrollTo(), click());
        onView(withId(R.id.bt_submit)).perform(scrollTo());
        onView(withId(R.id.sp_city)).perform(click());

        onData(allOf(is(instanceOf(City.class)))).atPosition(1).perform(click());
//                is("Kolkata"))).perform(click());
        onView(withId(R.id.sp_gender))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Male"))).perform(click());
        onView(withId(R.id.bt_submit)).perform(scrollTo(), click());

        registerIdlingResource();
        onView(withId(R.id.etOtp)).check(matches(isDisplayed()));
        OtpResponse response = idlingResource.getResponse();
        if (response != null) {
            onView(withId(R.id.etOtp)).perform(typeText(response.getOTP()), closeSoftKeyboard());
            onView(withId(R.id.btSubmit)).perform(click());
        }
    }

    public Activity getActivityInstance() {
        getInstrumentation().runOnMainSync(() -> {
            Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
            if (resumedActivities.iterator().hasNext()) {
                currentActivity = resumedActivities.iterator().next();
            }
        });

        return currentActivity;
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}
