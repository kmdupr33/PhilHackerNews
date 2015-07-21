package com.philosophicalhacker.philhackernews;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.philosophicalhacker.philhackernews.data.sync.DataSynchronizer;
import com.philosophicalhacker.philhackernews.ui.storieslist.StoriesActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTests {

    public static final String DUMMY_STORY_POINTS = "999+";

    public static final String DUMMY_STORY_TITLE = "Why Hacker News is Awesomer than your News";
    public static final String DUMMY_STORY_AUTHOR = "PhilosophicalHacker";
    public static final String DUMMY_URL = "http://www.philosophicalhacker.com";

    @Rule
    public ActivityTestRule<StoriesActivity> mActivityTestRule = new ActivityTestRule<>(StoriesActivity.class);

    @Inject
    DataSynchronizer mDataSynchronizer;

    @Inject
    File mHackerNewsDatabaseFile;

    private SyncAdapterIdlingResource mSyncAdapterIdlingResource;

    @Before
    public void registerIdlingResource() {
        PhilHackerNewsApplication application = (PhilHackerNewsApplication) mActivityTestRule.getActivity().getApplication();
        application.getObjectGraph().inject(this);
        mSyncAdapterIdlingResource = new SyncAdapterIdlingResource(mDataSynchronizer);
        Espresso.registerIdlingResources(mSyncAdapterIdlingResource);
    }

    @After
    public void unregisterIdlingResources() {
        Espresso.unregisterIdlingResources(mSyncAdapterIdlingResource);
        SQLiteDatabase.deleteDatabase(mHackerNewsDatabaseFile);
    }

    //----------------------------------------------------------------------------------
    // Tests
    //----------------------------------------------------------------------------------
    @Test
    public void loadHackerNewsPostsOnStartup() {
        onView(withClassName(containsString("RecyclerView"))).check(matches(isDisplayed()));
        verifyDummyDataIsDisplayed();
    }

    @Test
    public void dataStillDisplaysAfterOrientationChange() {
        onView(isRoot()).perform(OrientationChangeAction.orientationLandscape());
        verifyDummyDataIsDisplayed();
    }

    @Test
    public void showStoryDetailsWhenStoryIsTapped() {
        onView(withId(R.id.recyclerView)).perform(actionOnItemAtPosition(0, click()));
        onView(withText(DUMMY_STORY_TITLE)).check(matches(isDisplayed()));
        //TODO Test that proper web content is loaded
    }

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    private void verifyDummyDataIsDisplayed() {
        onView(withText(DUMMY_STORY_POINTS)).check(matches(isDisplayed()));
        onView(withText(DUMMY_STORY_TITLE)).check(matches(isDisplayed()));
        onView(withText(DUMMY_STORY_AUTHOR)).check(matches(isDisplayed()));
    }

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    private static class SyncAdapterIdlingResource implements IdlingResource {

        private ResourceCallback mResourceCallback;

        public SyncAdapterIdlingResource(DataSynchronizer dataSynchronizer) {
            mDataSynchronizer = dataSynchronizer;
        }

        private DataSynchronizer mDataSynchronizer;

        @Override
        public String getName() {
            return "SyncAdapterRequest";
        }

        @Override
        public boolean isIdleNow() {
            boolean idle = !mDataSynchronizer.isSyncActiveOrPending();
            if(idle) {
                mResourceCallback.onTransitionToIdle();
            }
            return idle;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
            mResourceCallback = resourceCallback;
        }
    }
}