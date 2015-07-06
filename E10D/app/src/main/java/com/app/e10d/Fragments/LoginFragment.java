package com.app.e10d.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.e10d.Interfaces.GeneralCallbacks;
import com.app.e10d.MainActivity;
import com.app.e10d.R;
import com.app.e10d.application.E10DApplication;
import com.app.e10d.constants.CommonMethods;
import com.app.e10d.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Nilesh on 07/07/15.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    EditText edtUserID,edtPassword;
    Button btnLogin;
    TextView txtCreateAccount;
    ProgressDialog pDialog;
    GeneralCallbacks mGeneralCallbacks;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View loginView = inflater.inflate(R.layout.login_layout,container,false);

        edtUserID = (EditText) loginView.findViewById(R.id.edtUserID);
        edtPassword = (EditText) loginView.findViewById(R.id.edtPassword);
        btnLogin = (Button) loginView.findViewById(R.id.btnLogin);
        txtCreateAccount = (TextView) loginView.findViewById(R.id.txtCreateAccount);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUserID != null && edtUserID.getText().toString().equalsIgnoreCase("")) {
                    edtUserID.setError("Username is required");
                } else if (edtPassword != null && edtPassword.getText().toString().equalsIgnoreCase("")) {
                    edtPassword.setError("Please enter Password");
                } else {
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("username", edtUserID.getText().toString());
                    data.put("pwd", edtPassword.getText().toString());
                    new LoginTask(data, getActivity()).execute(Constants.LOGIN_URL);

                }
            }
        });

        SpannableString createAccountString = new SpannableString(getString(R.string.create_account));
        createAccountString.setSpan(new UnderlineSpan(), 0, createAccountString.length(), 0);
        txtCreateAccount.setText(createAccountString);



        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGeneralCallbacks.switchFragment(MainActivity.FRAG_CREATE_ACCOUNT,new CreateAccountFragment());
            }
        });

        return loginView;
    }

    class LoginTask extends AsyncTask<String,Void,String> {

        HashMap<String,String> data;
        Context context;
        LoginTask(HashMap<String,String> data ,Context context){
            this.data = data;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (pDialog == null) {
                pDialog = new ProgressDialog(context);
            }

            pDialog.setMessage(getString(R.string.login_message));
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return CommonMethods.callSyncService(this.context, data, params[0], TAG);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(pDialog != null && pDialog.isShowing()){
                pDialog.dismiss();
            }

            if (s != null) {
                try {
                    JSONArray jsonArray = new JSONArray(s);

                    for(int i = 0; i < jsonArray.length(); i ++){
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        String id = jsonObject.optString("id");
                        String userName = jsonObject.optString("name");
                        if(!TextUtils.isEmpty(id) && !TextUtils.isEmpty(userName)){
                            E10DApplication.mPrefs.edit().putString(Constants.PREFS_ID, id).putString(Constants.PREFS_USER_NAME,userName).apply();
                            //Toast.makeText(MainActivity.this,"Welcome "+edtID.getText(),Toast.LENGTH_SHORT).show();
                            //startNextActivity();
                        }else{
                            mGeneralCallbacks.showErrorDialog("Error", "Error while signing in. Please try again");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                mGeneralCallbacks.showErrorDialog("Error", "Error while signing in. Please try again");
            }

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mGeneralCallbacks = (GeneralCallbacks) activity;
    }


}
