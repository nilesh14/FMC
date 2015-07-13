package com.fmc.v1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.constants.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{

	/*LoginButton loginButton;*/
	Button btnLoginWithFacebook;
	CallbackManager callbackManager;
	ProgressDialog pDialog;
	SharedPreferences mPrefs;
    TextView txtJoinGangText;
	
	FMCApplication app;
	private static String TAG = "LoginActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		callbackManager = CallbackManager.Factory.create();
		app = (FMCApplication) getApplication();
		mPrefs = app.getmPreffs();

        // Ttest Comment Git
		
		btnLoginWithFacebook = (Button) findViewById(R.id.btnLoginWithFacebook);
		btnLoginWithFacebook.setOnClickListener(this);
        txtJoinGangText = (TextView) findViewById(R.id.txtJoinGangText);
        paintJoinGangTextview(txtJoinGangText);
        /*loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setReadPermissions("user_friends,email,public_profile,user_location");*/
		pDialog = new ProgressDialog(this);
		
		//loginButton.registerCallback(callbackManager, facebookCallBack); 
		LoginManager.getInstance().registerCallback(callbackManager, facebookCallBack);
				
		
		updateToken(AccessToken.getCurrentAccessToken());
        accessTokenTracker.startTracking();
	}

    private void paintJoinGangTextview(TextView textView){
        String text = getString(R.string.Join_The_Gang);
        SpannableString spannableString = new SpannableString(text);
        int startIndexMom = text.indexOf("MOMS");
        int startIndexTrust = text.indexOf("TRUST");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Join_Gang_Text_Color)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.MOMS_color)), startIndexMom, startIndexMom + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.MOMS_color)), startIndexTrust, startIndexTrust + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setTypeface(FMCApplication.gotham);
        textView.setText(spannableString);
    }
	
	private void getUserData(final AccessToken accessToken){
		GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        // TODO Auto-generated method stub
                        if (object != null) {
                            Log.d(TAG, "Object " + object.toString());
                        }
						/*Log.v("LoginActivity", response.toString());*/

                        if (pDialog.isShowing()) {
                            pDialog.dismiss();
                        }

                        if (object != null) {
                            Log.d(TAG, "Object " + object.toString());
                            Editor editor = mPrefs.edit();
                            String name = object.optString("name");
                            if (!TextUtils.isEmpty(name)) {
                                editor.putString(Constants.PREFS_USER_NAME, name);
                            }
                            Constants.USER_NAME = name;
                            String email = object.optString("email");
                            if (!TextUtils.isEmpty(email)) {
                                editor.putString(Constants.PREFS_USER_EMAIL, email);
                            }
                            Constants.USER_EMAIL = email;

                            String id = object.optString("id");
                            if (!TextUtils.isEmpty(id)) {
                                editor.putString(Constants.PREFS_USER_ID, id);
                            }

                            String gender = object.optString("gender");
                            if (!TextUtils.isEmpty(gender)) {
                                editor.putString(Constants.PREFS_USER_GENDER, gender);
                            }

                            JSONObject jobj = object.optJSONObject("location");
                            if (jobj != null) {
                                String user_location = jobj.optString("name");
                                if (!TextUtils.isEmpty(user_location)) {
                                    editor.putString(Constants.PREFS_USER_LOCATION, user_location);
                                }
                            }
                            JSONObject jCover = object.optJSONObject("cover");
                            if (jCover != null) {
                                String coverImage = jCover.optString("source");
                                if (!TextUtils.isEmpty(coverImage)) {
                                    editor.putString(Constants.PREFS_COVER_IMAGE_URL,coverImage);
                                }
                            }

                            editor.apply();
                            Log.d(TAG, "Found Name  " + name + " Email " + email);
                            getFriendList();
                            //updateToken(accessToken);
                        } else {
                            showErrorDialog(getResources().getString(R.string.unable_to_get_info), "Alert!");
                        }


                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday,location,cover");
        request.setParameters(parameters);
        pDialog.setMessage("Fetching User Data");
        pDialog.setCanceledOnTouchOutside(false);
        if(!pDialog.isShowing()){
        	pDialog.show();	
        }
        
        request.executeAsync();
	}

    GraphRequestBatch batch = new GraphRequestBatch(GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONArrayCallback() {
        @Override
        public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
            if (jsonArray != null) {
                Log.d(TAG, "Got firends List : " + jsonArray.toString());

            }

            updateToken(AccessToken.getCurrentAccessToken());
        }
    }));

	private void getFriendList() {
		batch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch graphRequestBatch) {

            }
        });

        batch.executeAsync();
	}
	
	FacebookCallback<LoginResult> facebookCallBack = new FacebookCallback<LoginResult>() {
		
		@Override
		public void onSuccess(LoginResult loginResult) {
			// TODO Auto-generated method stub
			if(loginResult != null){
        		loginResult.getAccessToken();
                FMCApplication.loggedinUserPic = null;
        		getUserData(loginResult.getAccessToken());
        	}
		}
		
		@Override
		public void onError(FacebookException error) {
			// TODO Auto-generated method stub
			if(pDialog != null && pDialog.isShowing()){
            	pDialog.dismiss();
            }
			showErrorDialog(getResources().getString(R.string.error_while_get_info),"Alert!");
		}
		
		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			if(pDialog != null && pDialog.isShowing()){
            	pDialog.dismiss();
            }
			showErrorDialog(getResources().getString(R.string.unable_to_get_info), "Alert!");
		}
	};

	AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
            //updateToken(newAccessToken);
        }
    };

    private void updateToken(AccessToken accessToken){
        if(accessToken != null){
            startActivity(new Intent(this,CodeValidationActivity.class));
            this.finish();
        }else{
            Toast.makeText(this,"Please Login With Facebook",Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showErrorDialog(String msg,String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title==null?"Alert":title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        if(accessTokenTracker.isTracking()){
            accessTokenTracker.stopTracking();
        }
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnLoginWithFacebook:
			pDialog.setMessage("Logging in with Facebook. Please wait...");
			pDialog.show();
			LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_friends,email,public_profile,user_location"));
			break;

		default:
			break;
		}
	}
}
