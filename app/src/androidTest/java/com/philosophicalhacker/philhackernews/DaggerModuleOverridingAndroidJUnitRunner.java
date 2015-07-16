package com.philosophicalhacker.philhackernews;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;
import android.util.Log;

/**
 * Created by MattDupree on 7/16/15.
 */
public class DaggerModuleOverridingAndroidJUnitRunner extends AndroidJUnitRunner {

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String canonicalName = TestApplication.class.getCanonicalName();
        Log.d("class name", canonicalName);
        return super.newApplication(cl, canonicalName, context);
    }
}
