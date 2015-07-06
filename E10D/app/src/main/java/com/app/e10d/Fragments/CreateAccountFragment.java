package com.app.e10d.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class CreateAccountFragment extends Fragment {

    private static final String TAG = "CreateAccountFragment";
    EditText edtFullName,edtEmailID,edtAddress,edtCell,edtPassword;
    Button btnRegister;
    TextView txtLoginToAccount;
    GeneralCallbacks mGeneralCallBacks;
    ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_new_account_layout,container,false);

        edtFullName = (EditText) view.findViewById(R.id.edtFullName);
        edtEmailID = (EditText) view.findViewById(R.id.edtEmailID);
        edtAddress = (EditText) view.findViewById(R.id.edtAddress);
        edtCell = (EditText) view.findViewById(R.id.edtCell);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);

        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        txtLoginToAccount = (TextView) view.findViewById(R.id.txtLoginToAccount);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtFullName.getText().toString().equalsIgnoreCase("")){
                    edtFullName.setError("Please enter Name");
                }else if(edtEmailID.getText().toString().equalsIgnoreCase("")){
                    edtEmailID.setError("Please enter your email");
                }else if(edtAddress.getText().toString().equalsIgnoreCase("")){
                    edtAddress.setError("Please enter your address");
                }else if(edtCell.getText().toString().equalsIgnoreCase("")){
                    edtCell.setError("Please enter your Cell");
                }else if(edtPassword.getText().toString().equalsIgnoreCase("")){
                    edtPassword.setError("Please enter password");
                }else{
                    HashMap<String,String> data = new HashMap<String, String>();
                    data.put("name",edtFullName.getText().toString());
                    data.put("email",edtEmailID.getText().toString());
                    data.put("address",edtAddress.getText().toString());
                    data.put("cell",edtCell.getText().toString());
                    data.put("pwd",edtPassword.getText().toString());

                    new CreateAccountTask(data,getActivity()).execute(Constants.REGISTER_URL);
                }
            }
        });

        txtLoginToAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGeneralCallBacks.switchFragment(MainActivity.FRAG_LOGIN,new LoginFragment());
            }
        });

        return view;
    }

    class CreateAccountTask extends AsyncTask<String,Void,String> {

        HashMap<String,String> data;
        Context context;
        CreateAccountTask(HashMap<String,String> data ,Context context){
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
                        String id = jsonObject.optString("uid");
                        String userName = jsonObject.optString("name");
                        if(!TextUtils.isEmpty(id) && !TextUtils.isEmpty(userName)){
                            E10DApplication.mPrefs.edit().putString(Constants.PREFS_ID, id).putString(Constants.PREFS_USER_NAME,userName).apply();
                            //Toast.makeText(MainActivity.this,"Welcome "+edtID.getText(),Toast.LENGTH_SHORT).show();
                            //startNextActivity();
                        }else{
                            mGeneralCallBacks.showErrorDialog("Error", "Error while signing in. Please try again");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                mGeneralCallBacks.showErrorDialog("Error", "Error while signing in. Please try again");
            }

        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mGeneralCallBacks = (GeneralCallbacks) activity;
    }
}
