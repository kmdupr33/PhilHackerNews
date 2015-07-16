package com.philosophicalhacker.philhackernews;

import android.content.Context;

import com.philosophicalhacker.philhackernews.daggermodules.PhilHackerNewsAppModule;

/**
 * Created by MattDupree on 7/16/15.
 */
public class Modules {
    public static Object[] list(Context applicationContext) {
        return new Object[] {new PhilHackerNewsAppModule(applicationContext)};
    }
}
