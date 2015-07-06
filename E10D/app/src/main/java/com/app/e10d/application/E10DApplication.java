package com.app.e10d.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nilesh on 06/07/15.
 */
public class E10DApplication extends Application {

    public static SharedPreferences mPrefs;
    @Override
    public void onCreate() {
        super.onCreate();
        mPrefs = getSharedPreferences("com.app.e10d", Context.MODE_PRIVATE);
    }
}
