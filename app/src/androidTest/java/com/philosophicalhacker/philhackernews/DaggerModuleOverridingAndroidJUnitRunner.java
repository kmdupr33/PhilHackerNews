package com.philosophicalhacker.philhackernews;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * Created by MattDupree on 7/16/15.
 */
public class DaggerModuleOverridingAndroidJUnitRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String canonicalName = TestApplication.class.getCanonicalName();
        return super.newApplication(cl, canonicalName, context);
    }
}
