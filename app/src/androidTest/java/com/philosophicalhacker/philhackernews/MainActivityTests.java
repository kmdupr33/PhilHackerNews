package com.philosophicalhacker.philhackernews;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncInfo;
import android.content.SyncStatusObserver;
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

    @Test
    public void loadHackerNewsPostsOnStartup() throws InterruptedException {
        onView(withClassName(containsString("RecyclerView"))).check(matches(isDisplayed()));
        onView(withText("9897306")).check(matches(isDisplayed()));
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
            if(ContentResolver.isSyncActive(null, HackerNewsData.CONTENT_AUTHORITY)) {
                return true;
            }
            return false;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
            mResourceCallback = resourceCallback;
        }
    }
}