package com.philosophicalhacker.philhackernews;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * Created by MattDupree on 7/16/15.
 */
public class DaggerModuleOverridingAndroidJUnitRunner extends AndroidJUnitRunner {

    private Application mApplication;

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String testApplicationClassName = TestApplication.class.getCanonicalName();
        mApplication = super.newApplication(cl, testApplicationClassName, context);
        return mApplication;
    }

    public TestApplication getApplication() {
        return (TestApplication) mApplication;
    }
}
