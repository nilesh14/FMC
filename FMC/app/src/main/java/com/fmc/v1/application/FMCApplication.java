package com.fmc.v1.application;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.fmc.v1.BuildConfig;
import com.fmc.v1.R;
import com.fmc.v1.constants.Constants;
import com.fmc.v1.data.UserData;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;

public class FMCApplication extends Application {
    public static SharedPreferences mPreffs;
    public static Typeface gotham, daydreamer, ubuntu;
    public static UserData loggedinUserData;
    public static Bitmap loggedinUserPic, loggedinUserCoverPic;
    String facebookProfileURL,facebookCoverURL;
    public static DisplayImageOptions options;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        loggedinUserData = new UserData();
        //facebookProfileURL = "https://graph.facebook.com/" + AccessToken.getCurrentAccessToken().getUserId() + "/picture?type=large";
        mPreffs = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        gotham = Typeface.createFromAsset(getAssets(), "Gotham-Rounded-Book_21018.ttf");
        daydreamer = Typeface.createFromAsset(getAssets(), "Daydreamer.ttf");
        ubuntu = Typeface.createFromAsset(getAssets(), "Ubuntu_L.ttf");
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.contact)
                .showImageForEmptyUri(R.drawable.contact)
                .showImageOnFail(R.drawable.contact)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        initImageLoader(getApplicationContext());


    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public String makeFacebookProfileURL(String userID) {
        facebookProfileURL = "https://graph.facebook.com/" + userID + "/picture?type=large";
        return facebookProfileURL;
    }

    public void setFacebookProfileURL(String facebookProfileURL) {
        this.facebookProfileURL = facebookProfileURL;
    }

    public String makeFacebookCoverURL() {
        return facebookCoverURL;
    }

    public void setFacebookCoverURL(String facebookCoverURL) {
        this.facebookCoverURL = facebookCoverURL;
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

    public static Typeface getUbuntu() {
        return ubuntu;
    }
}
