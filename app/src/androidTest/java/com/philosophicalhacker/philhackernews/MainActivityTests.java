package com.philosophicalhacker.philhackernews;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncInfo;
import android.content.SyncStatusObserver;
import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.philosophicalhacker.philhackernews.data.content.HackerNewsData;
import com.philosophicalhacker.philhackernews.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTests {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private SyncAdapterIdlingResource mSyncAdapterIdlingResource;

    @Before
    public void registerIdlingResource() {
        mSyncAdapterIdlingResource = new SyncAdapterIdlingResource();
        Espresso.registerIdlingResources(mSyncAdapterIdlingResource);
    }

    @After
    public void unregisterIdlingResources() {
        Espresso.unregisterIdlingResources(mSyncAdapterIdlingResource);
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

    //----------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------
    private void verifyDummyDataIsDisplayed() {
        onView(withText("99+")).check(matches(isDisplayed()));
        onView(withText("Why Hacker News is Awesomer than your News")).check(matches(isDisplayed()));
        onView(withText("PhilosophicalHacker")).check(matches(isDisplayed()));
    }

    //----------------------------------------------------------------------------------
    // Nested Inner Class
    //----------------------------------------------------------------------------------
    private static class SyncAdapterIdlingResource implements IdlingResource {
        public ResourceCallback mResourceCallback;

        @Override
        public String getName() {
            return "SyncAdapterRequest";
        }

        @Override
        public boolean isIdleNow() {
            boolean idle = isSyncComplete();
            if(idle) {
                mResourceCallback.onTransitionToIdle();
            }
            return idle;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
            mResourceCallback = resourceCallback;
        }

        //----------------------------------------------------------------------------------
        // Helpers
        //----------------------------------------------------------------------------------
        private boolean isSyncComplete() {
            return !ContentResolver.isSyncPending(MainActivity.mAccount, HackerNewsData.CONTENT_AUTHORITY)
                    && !ContentResolver.isSyncActive(MainActivity.mAccount, HackerNewsData.CONTENT_AUTHORITY);
        }
    }
}