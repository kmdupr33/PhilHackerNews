package com.philosophicalhacker.philhackernews.ui.refresh;

import android.support.annotation.IntDef;

/**
 *
 * Created by MattDupree on 7/21/15.
 */
public interface RefreshStatusListener {

    @IntDef({REFRESHING, NOT_REFRESHING})
    @interface RefreshStatus {}
    int NOT_REFRESHING = 0;
    int REFRESHING = 1;

    void onRefreshingStatusChanged(@RefreshStatus int status);
}
