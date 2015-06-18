package com.fmc.v1.application;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.fmc.v1.BuildConfig;
import com.fmc.v1.R;
import com.fmc.v1.constants.Constants;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

public class FMCApplication extends Application{
	public static SharedPreferences mPreffs;
	public static Typeface gotham,daydreamer;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mPreffs = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		FacebookSdk.sdkInitialize(getApplicationContext());
		gotham = Typeface.createFromAsset(getAssets(), "Gotham-Rounded-Book_21018.ttf");
		daydreamer = Typeface.createFromAsset(getAssets(), "Daydreamer.ttf");
		if (BuildConfig.DEBUG) {
		    FacebookSdk.setIsDebugEnabled(true);
		    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		}

	}
	public SharedPreferences getmPreffs() {
		return mPreffs;
	}
	public Typeface getGotham() {
		return gotham;
	}
	public Typeface getDaydreamer() {
		return daydreamer;
	}
	

}
