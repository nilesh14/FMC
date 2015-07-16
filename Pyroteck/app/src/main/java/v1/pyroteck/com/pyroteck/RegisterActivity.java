package v1.pyroteck.com.pyroteck;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import v1.pyroteck.com.pyroteck.application.PyrotekApplication;
import v1.pyroteck.com.pyroteck.constants.CommonMethods;
import v1.pyroteck.com.pyroteck.constants.Constants;

/**
 * Created by Nilesh on 16/05/15.
 */
public class RegisterActivity extends Activity {

    private static final String TAG = "RegisterActivity";
    TextView txtSkip,txtRegister;
    Button btnRegister;
    EditText edtYourName,edtOfficialEmail,edtCompanyName;
    PyrotekApplication app;
    SharedPreferences mPrefs;
    ProgressDialog pDialog;
    HashMap<String,String> mapData;
    String url,dialogMsg,successMsg,errorMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        app = (PyrotekApplication) getApplication();
        mPrefs = app.getmPrefs();
        edtYourName = (EditText) findViewById(R.id.edtYourName);
        edtOfficialEmail = (EditText) findViewById(R.id.edtOfficialEmail);
        edtCompanyName = (EditText) findViewById(R.id.edtCompanyName);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        mapData = new HashMap<String,String>();

        if(mPrefs.getBoolean(PyrotekApplication.IS_USER_REGISTERED,false)){
            btnRegister.setText(getString(R.string.update));
            url = Constants.UPDATE_USER_URL;
            dialogMsg = getString(R.string.update_dialog_message);
            successMsg = getString(R.string.update_dialog_message_success);
            errorMsg = getString(R.string.update_error);
        }else{
            btnRegister.setText(getString(R.string.register));
            url = Constants.REGISTER_USER_URL;
            dialogMsg = getString(R.string.register_dialog_message);
            successMsg = getString(R.string.register_dialog_message_success);
            errorMsg = getString(R.string.register_error);
        }


        txtSkip = (TextView) findViewById(R.id.txtSkip);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        txtRegister.setPaintFlags(txtRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtSkip.setPaintFlags(txtSkip.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

        if (app != null) {
            if(app.getmPrefs().getBoolean(PyrotekApplication.IS_USER_REGISTERED,false)){
                Log.d(TAG,"User ID : "+app.getmPrefs().getInt(PyrotekApplication.UID,0));
                startMainActivity();
            }
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtYourName.getText().toString())) {
                    showDialog(getString(R.string.name_error  ));
                }else if(TextUtils.isEmpty(edtOfficialEmail.getText().toString())){
                    showDialog(getString(R.string.official_email_error));
                }else if(TextUtils.isEmpty(edtCompanyName.getText().toString())){
                    showDialog(getString(R.string.company_name_error));
                }else{

                    mapData.clear();
                    mapData.put("name", edtYourName.getText().toString());
                    mapData.put("email", edtOfficialEmail.getText().toString());
                    mapData.put("company", edtCompanyName.getText().toString());
                    new RegisterNewUser(RegisterActivity.this,mapData,dialogMsg).execute(Constants.REGISTER_USER_URL);

                }
            }
        });
    }

    private void startMainActivity(){
        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
        finish();
    }

    class RegisterNewUser extends AsyncTask<String,Void,String>{

        private static final String TAG = "RegisterNewUser";
        Context context;
        HashMap<String,String> data;
        String msg;
        public RegisterNewUser(Context context,HashMap<String,String> data,String msg) {
            this.context = context;
            this.data = data;
            this.msg = msg;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(pDialog == null){
                pDialog = new ProgressDialog(context);
            }
            pDialog.setMessage(msg);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG,"URL is "+params[0]);
            return CommonMethods.callSyncService(context,data,params[0], TAG);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            if (s != null) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        int code = jObj.optInt("code");
                        if(code > 0){
                            SharedPreferences.Editor editor = mPrefs.edit();

                            editor.putString(PyrotekApplication.COMPANY_NAME,edtCompanyName.getText().toString())
                                    .putString(PyrotekApplication.OFFICIAL_EMAIL,edtOfficialEmail.getText().toString())
                                    .putString(PyrotekApplication.USER_NAME,edtYourName.getText().toString())
                                    .putInt(PyrotekApplication.UID,code)
                                    .putBoolean(PyrotekApplication.IS_USER_REGISTERED, true).apply();
                            Toast.makeText(RegisterActivity.this,successMsg,Toast.LENGTH_LONG).show();
                            startMainActivity();
                        }else{
                            Toast.makeText(context,errorMsg,Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,errorMsg,Toast.LENGTH_SHORT).show();
                }


            }else{
                Toast.makeText(context,getString(R.string.no_internet_or_error),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog(String msg){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
        dialogBuilder.setTitle(getResources().getString(R.string.alert));
        dialogBuilder.setMessage(msg == null ? "" : msg);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.show();
    }
}
