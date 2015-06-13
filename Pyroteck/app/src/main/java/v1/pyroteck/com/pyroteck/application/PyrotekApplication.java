package v1.pyroteck.com.pyroteck.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.parse.Parse;
import com.parse.ParseInstallation;

import v1.pyroteck.com.pyroteck.DataHolder;
import v1.pyroteck.com.pyroteck.R;

/**
 * Created by Nilesh on 01/05/15.
 */
public class PyrotekApplication extends Application {

    SharedPreferences mPrefs;

    public static final String IS_USER_REGISTERED = "is_user_registered";
    public static final String USER_NAME = "user_name";
    public static final String OFFICIAL_EMAIL= "official_email";
    public static final String COMPANY_NAME= "comapany_name";
    public static final String UID= "uid";

    @Override
    public void onCreate() {
        super.onCreate();
        DataHolder.prepareData();
        Log.d("Application Class", "Data prepared");
        mPrefs = getSharedPreferences("com.pyroteck", Context.MODE_PRIVATE);
        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public SharedPreferences getmPrefs() {
        return mPrefs;
    }
}
