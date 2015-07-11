package com.app.e10d;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.e10d.Fragments.CreateAccountFragment;
import com.app.e10d.Fragments.LoginFragment;
import com.app.e10d.Interfaces.GeneralCallbacks;
import com.app.e10d.constants.CommonMethods;
import com.app.e10d.constants.Constants;
import com.app.e10d.application.E10DApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends FragmentActivity implements GeneralCallbacks {

    private static final String TAG = "MainActivity";
    EditText edtUserID,edtPassword;
    Button btnLogin;
    TextView txtCreateAccount;
    ProgressDialog pDialog;
    LinearLayout linLayoutContainer,linLoginLayout;
    Animation slideInLeft,slideInRight,slideOutLeft,slideOutRight;
    LayoutInflater inflater;

    public static int FRAG_LOGIN = 1;
    public static int FRAG_CREATE_ACCOUNT = 2;

    int activeFragment = 0;

    View loginView,createAccountView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(E10DApplication.mPrefs.getString(Constants.PREFS_ID,null)!=null){
            Log.d(TAG,"Logged in User id = "+E10DApplication.mPrefs.getString(Constants.PREFS_ID,""));
            startNewActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
            finish();
            return;
        }
        //linLayoutContainer = (LinearLayout) findViewById(R.id.linLayoutContainer);
        slideInLeft = AnimationUtils.loadAnimation(MainActivity.this,R.anim.slide_in_left);
        slideInRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_right);
        slideOutLeft = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_left);
        slideOutRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right);

        switchFragment(FRAG_LOGIN,new LoginFragment());
    }

    private void switchFragment(int fragID){

    }


    private void startNextActivity() {

    }

    @Override
    public void showErrorDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK",null);
        builder.show();
    }

    @Override
    public void switchFragment(int fragID, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(fragID == FRAG_LOGIN){
            getSupportFragmentManager().popBackStack();
            ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_left);
            ft.replace(R.id.frameContainer,fragment).commit();
            activeFragment = FRAG_LOGIN;
        }else if(fragID == FRAG_CREATE_ACCOUNT){
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_right);
            ft.replace(R.id.frameContainer,fragment).addToBackStack("CREATE_ACCOUNT").commit();
            activeFragment = FRAG_CREATE_ACCOUNT;
        }
    }

    @Override
    public void startNewActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void updateCartCount(int count) {

    }

    @Override
    public int getCartCountAndUpdate() {
        return 0;
    }


}
