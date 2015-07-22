package com.philosophicalhacker.philhackernews.ui.refresh;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * A SwipeRefreshLayout that can have a child other than its immediate child. Here I follow
 * the same approach Roman Nurik did in the Google's IOSched app.
 *
 * See https://github.com/google/iosched/blob/b3c3ae2a5b41e28c07383644d78e5c076288322f/android/src/main/java/com/google/samples/apps/iosched/ui/SessionsFragment.java
 *
 * Created by MattDupree on 7/21/15.
 */
public class CustomChildSwipeRefreshLayout extends SwipeRefreshLayout {

    private View mCustomChild;

    public CustomChildSwipeRefreshLayout(Context context) {
        super(context);
    }

    public CustomChildSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCustomChild(View customChild) {
        mCustomChild = customChild;
    }

    @Override
    public boolean canChildScrollUp() {
        if (mCustomChild != null) {
            return ViewCompat.canScrollVertically(mCustomChild, -1);
        }
        return super.canChildScrollUp();
    }
}
