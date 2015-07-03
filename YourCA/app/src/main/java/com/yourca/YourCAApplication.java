package com.yourca;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nilesh on 03/07/15.
 */
public class YourCAApplication extends Application {

    static SharedPreferences mPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        mPrefs = getSharedPreferences("com.yourca", Context.MODE_PRIVATE);
    }
}
