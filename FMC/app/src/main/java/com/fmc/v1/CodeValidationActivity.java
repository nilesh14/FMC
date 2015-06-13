package com.fmc.v1;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.constants.CommonMethods;
import com.fmc.v1.constants.Constants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


/**
 * Created by Nilesh on 10/05/15.
 */
public class CodeValidationActivity extends Activity {

	Button edtEnterRequestCode,btnAddMommyFriends;
	EditText edtEnterCode;
	ProgressDialog pDialog;
	TextView txtEnterCode;
	HashMap<String, String> mapData = new HashMap<String, String>();
	public static final String TAG = "CodeValidationActivity";
	SharedPreferences mPrefs;
	FMCApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.code_validation_layout);
    	
    	app = (FMCApplication) getApplication();
    	txtEnterCode = (TextView) findViewById(R.id.txtEnterCode);
    	mPrefs = app.getmPreffs();
    	btnAddMommyFriends = (Button) findViewById(R.id.btnAddMommyFriends);
    	edtEnterCode = (EditText) findViewById(R.id.edtEnterCode);
    	pDialog = new ProgressDialog(CodeValidationActivity.this);
    	Typeface gotham = Typeface.createFromAsset(getAssets(), "Gotham-Rounded-Book_21018.ttf");
    	btnAddMommyFriends.setTypeface(gotham);
    	txtEnterCode.setTypeface(gotham);

        if(FMCApplication.mPreffs.getBoolean(Constants.PREFS_CODE_VALIDATION_DONE,false)){
            startWallActivity();
        }
    	
    	edtEnterCode.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				Log.d(TAG,"Got action value "+actionId);
				if((actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_SEND || actionId == 0) ){
					mapData.clear();
					mapData.put(Constants.KEY_VALUE, edtEnterCode.getText().toString());
					mapData.put(Constants.KEY_EMAIL, mPrefs.getString(Constants.PREFS_USER_EMAIL, ""));
					mapData.put(Constants.KEY_NAME, mPrefs.getString(Constants.PREFS_NAME, ""));
					mapData.put(Constants.KEY_CITY, "Mumbai");
					new GetValidationCodeResult(mapData).execute(Constants.GET_VALIDATION_CODE_URL);
				}
				
				return true;
			}
		});
    	
    	/*edtEnterRequestCodeCode = (Button) findViewById(R.id.edtEnterRequestCodeCode);
    	
    	edtEnterRequestCodeCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "UnderConstruction", Toast.LENGTH_SHORT).show();
			}
		});*/
    }
    
    class GetValidationCodeResult extends AsyncTask<String, Void, String>{

    	HashMap<String, String> data;
    	public GetValidationCodeResult(HashMap<String, String> data) {
			// TODO Auto-generated constructor stub
    		this.data = data;
		}
    	
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		
    		if(pDialog == null){
				pDialog = new ProgressDialog(CodeValidationActivity.this);	
				pDialog.dismiss();
			}
			pDialog.setCancelable(false);
			pDialog.setMessage("Validation in progress");
			pDialog.show();
    	}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			return CommonMethods.callSyncService(getApplicationContext(), data, params[0], TAG);
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			
			try {
				if(pDialog != null && pDialog.isShowing()){
					pDialog.dismiss();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(result != null){
				try {
					JSONArray jArr = new JSONArray(result);
					for(int i = 0; i < jArr.length(); i++){
						JSONObject jOBJ = jArr.getJSONObject(i);
						int code = jOBJ.optInt("code");
						if(code > 0){
							mPrefs.edit().putInt(Constants.PREFS_UID, code).apply();

                            FMCApplication.mPreffs.edit().putBoolean(Constants.PREFS_CODE_VALIDATION_DONE,true).apply();
                            startWallActivity();
						}else{
							Toast.makeText(getApplicationContext(), "Invalid Validation Code.Please try again!", Toast.LENGTH_LONG).show();							
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Error Parsing response.Please try again!", Toast.LENGTH_LONG).show();
					
					// Remove this 
					Toast.makeText(getApplicationContext(), "Your in!", Toast.LENGTH_LONG).show();
					startActivity(new Intent(CodeValidationActivity.this, MainActivity.class));
					finish();
				}
			}else{
                Toast.makeText(CodeValidationActivity.this,getString(R.string.no_internet_or_error),Toast.LENGTH_SHORT).show();
            }
		}
    	
    }

    private void startWallActivity(){
        Toast.makeText(getApplicationContext(), "Your in!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(CodeValidationActivity.this, MainActivity.class));
        finish();
    }
    
}
