package v1.pyroteck.com.pyroteck;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import v1.pyroteck.com.pyroteck.application.PyrotekApplication;
import v1.pyroteck.com.pyroteck.callbacks.CategoryFragmentCallBacks;
import v1.pyroteck.com.pyroteck.callbacks.DescriptionCallBack;
import v1.pyroteck.com.pyroteck.callbacks.NewsCallBack;
import v1.pyroteck.com.pyroteck.callbacks.SubCategoryCallBacks;
import v1.pyroteck.com.pyroteck.constants.CommonMethods;
import v1.pyroteck.com.pyroteck.constants.Constants;
import v1.pyroteck.com.pyroteck.data.SubCategory;


public class MainActivity extends ActionBarActivity implements CategoryFragmentCallBacks,SubCategoryCallBacks,NewsCallBack,DescriptionCallBack{

    public static final int FRAG_CATEGORY = 1;
    public static final int FRAG_SUBCATEGORY = 2;
    public static final int FRAG_DESCRIPTION = 3;
    public static final int FRAG_SEARCH = 4;
    public static final int FRAG_NEWS = 5;
    public static final int FRAG_SETTINGS = 6;
    private static final String TAG = "MainActivity";
    int activeFragment = 0 ;
    LinearLayout linProducts,linSearch,linNews,linSetting;
    PyrotekApplication app;
    SharedPreferences mPrefs;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();

        app = (PyrotekApplication) getApplication();
        mPrefs = app.getmPrefs();

        Log.d(TAG, "onCreate");
        List<String> listChannels = ParseInstallation.getCurrentInstallation().getList("channels");
        if (listChannels == null || !listChannels.contains(Constants.GLOBAL_NOTIFICATION_CHANNEL)) {
            ParsePush.subscribeInBackground(Constants.GLOBAL_NOTIFICATION_CHANNEL, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.d(TAG, "Error Global Channel : "+e.getMessage());
                    } else {
                        Log.d(TAG, "Subscribed to Global Channel");
                    }
                }
            });
        }else{
            Log.d(TAG,"Already Subscribed to Global Channel");
        }


        linProducts = (LinearLayout) findViewById(R.id.linProducts);
        linSearch = (LinearLayout) findViewById(R.id.linSearch);
        linNews = (LinearLayout) findViewById(R.id.linNews);
        linSetting = (LinearLayout) findViewById(R.id.linSetting);

        linSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(FRAG_SEARCH);
            }
        });

        linProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyBackStack();

                switchFragment(FRAG_CATEGORY);
            }


        });

        linNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(FRAG_NEWS);
            }
        });


        linSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(FRAG_SETTINGS);
            }
        });
        switchFragment(FRAG_CATEGORY);
        processIntent(getIntent());
    }

    private  void emptyBackStack(){
        FragmentManager fm = getFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    private  void emptyNewsFragmentAndBackStack(){
        FragmentManager fm = getFragmentManager();

        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    Runnable showNews = new Runnable() {
        @Override
        public void run() {
            switchFragment(FRAG_NEWS);
        }
    };

    public void switchFragment(int fragid){
        resetColor();
        activeFragment = fragid;
        if(fragid == FRAG_CATEGORY){
            emptyBackStack();
            linProducts.setBackgroundColor(getResources().getColor(R.color.red_selected));
            getFragmentManager().beginTransaction().replace(R.id.frame,new CategoryFragment()).commit();
        }else if(fragid == FRAG_SEARCH){
            linSearch.setBackgroundColor(getResources().getColor(R.color.red_selected));
            emptyBackStack();
            getFragmentManager().beginTransaction().replace(R.id.frame,new SearchFragment()).commit();
        }else if(fragid == FRAG_NEWS){
            linNews.setBackgroundColor(getResources().getColor(R.color.red_selected));
            emptyBackStack();
            getFragmentManager().beginTransaction().replace(R.id.frame,new NewsFragment()).commit();
        }else{
            linSetting.setBackgroundColor(getResources().getColor(R.color.red_selected));
            emptyBackStack();
            getFragmentManager().beginTransaction().replace(R.id.frame,new SettingsFragment()).commit();
        }


    }

    private void resetColor(){
        linProducts.setBackgroundColor(getResources().getColor(R.color.red));
        linNews.setBackgroundColor(getResources().getColor(R.color.red));
        linSearch.setBackgroundColor(getResources().getColor(R.color.red));
        linSetting.setBackgroundColor(getResources().getColor(R.color.red));
    }

    @Override
    public void switchToSubcategoryFragment(String selectedCategory) {
        SubCategoryFragment subCategoryFragment = new SubCategoryFragment();

        Bundle bundle = new Bundle();
        bundle.putString("dataString", selectedCategory);

        subCategoryFragment.setArguments(bundle);
        activeFragment = FRAG_SUBCATEGORY;
        getFragmentManager().beginTransaction().replace(R.id.frame,subCategoryFragment).addToBackStack("SUBCATEGORY").commit();
    }

    @Override
    public void switchToDescriptionFragment(SubCategory subCategory) {
        DescriptionFragment descriptionFragment = new DescriptionFragment();

        Bundle bundle = new Bundle();

        bundle.putSerializable("dataSubcategory", subCategory);

        descriptionFragment.setArguments(bundle);
        activeFragment = FRAG_DESCRIPTION;
        getFragmentManager().beginTransaction().replace(R.id.frame,descriptionFragment).addToBackStack("DESCRIPTION").commit();

    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0){
            super.onBackPressed();
        }else{
            getFragmentManager().popBackStack();
        }


    }

    @Override
    public void switchToNewsDetailFragment(String id) {
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        newsDetailFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.frame,newsDetailFragment).addToBackStack("NEWS_DETAIL").commit();
    }

    @Override
    public void sendUsageRequest(SubCategory subCategory) {
        final String title = null;
        subCategory.getTitle();
        if (subCategory != null) {
            new Thread(new Runnable() {
                public static final String TAG = "Thread";

                @Override
                public void run() {
                    if(mPrefs.getInt(PyrotekApplication.UID, 0) != 0){
                        HashMap<String,String> mapData = new HashMap<String, String>();
                        mapData.clear();
                        mapData.put("uid", String.valueOf(mPrefs.getInt(PyrotekApplication.UID, 0)));
                        mapData.put("product",title);
                        Log.d(TAG,"Sending Usage Request");
                        CommonMethods.callSyncService(getApplicationContext(), mapData, Constants.REGISTER_USAGE_EVENT_URL, "Send Request Call");
                    }
                }
            }).start();
        }
    }

    private void processIntent(Intent intent){

        if (intent != null && intent.getExtras() != null) {
            JSONObject json = null;
            Log.d(TAG,"onNewIntent");
            try {
                String action = intent.getAction();
                String channel = intent.getExtras().getString("com.parse.Channel");
                json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

                Log.d(TAG, "got action " + action + " on channel " + channel + " with:"+"JSON Object "+json.toString());
                Iterator itr = json.keys();
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    if(key.equalsIgnoreCase("alert")){
                        //switchFragment(FRAG_NEWS);
                        handler.postDelayed(showNews,500);
                        break;
                    }
                    Log.d(TAG, "..." + key + " => " + json.getString(key));
                }
            } catch (JSONException e) {
                //Log.d(TAG, "JSONException: " + e.getMessage());
            }

        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "Got New Intent");
        processIntent(intent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(showNews);
        handler = null;
    }

    /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
