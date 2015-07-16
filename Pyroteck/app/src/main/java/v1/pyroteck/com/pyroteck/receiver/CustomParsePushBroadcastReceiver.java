package v1.pyroteck.com.pyroteck.receiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import v1.pyroteck.com.pyroteck.MainActivity;

/**
 * Created by Nilesh on 27/05/15.
 */
public class CustomParsePushBroadcastReceiver extends ParsePushBroadcastReceiver {


    private static final String TAG = "CustomBroadcastReceiver";

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        JSONObject json = null;
        Log.d(TAG,"onPushReceive");
        try {
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.parse.Channel");
            json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.d(TAG, "got action " + action + " on channel " + channel + " with:"+"JSON Object "+json.toString());
            Iterator itr = json.keys();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                Log.d(TAG, "..." + key + " => " + json.getString(key));
            }
        } catch (JSONException e) {
            //Log.d(TAG, "JSONException: " + e.getMessage());
        }
    }



    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {

        //return super.getActivity(context, intent);
        return MainActivity.class;
    }
}
