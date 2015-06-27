package com.fmc.v1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.facebook.AccessToken;
import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.constants.CommonMethods;
import com.fmc.v1.constants.Constants;
import com.fmc.v1.data.ChildData;
import com.fmc.v1.data.UserData;
import com.fmc.v1.view.CircularImageView;
import com.fmc.v1.view.DateDisplayPicker;

/**
 * Created by Nilesh on 27/06/15.
 */
public class EditProfileActivity extends Activity {

    private static final String TAG = "EditProfileActivity";
    Button btnDone,btnCancel;

    EditText edtHashtag,edtBio,edtWebsite, edtBitchUserName,edtBitchPassword;
    DateDisplayPicker txtBirthday;
    ImageView imgAddChild;
    CircularImageView imgPic;
    LinearLayout linChildrenDetailContainer;
    LayoutInflater layoutInflater;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        btnDone = (Button) findViewById(R.id.btnDone);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        edtHashtag = (EditText) findViewById(R.id.edtHashtag);
        edtBio = (EditText) findViewById(R.id.edtBio);
        edtWebsite = (EditText) findViewById(R.id.edtWebsite);
        edtBitchUserName = (EditText) findViewById(R.id.edtBitchUserName);
        edtBitchPassword = (EditText) findViewById(R.id.edtBitchPassword);
        layoutInflater = LayoutInflater.from(this);

        if(FMCApplication.mPreffs.getBoolean(Constants.PREFS_PROFILE_SET,false)){
            startNextActivity();
        }

        txtBirthday = (DateDisplayPicker) findViewById(R.id.txtBirthday);

        imgAddChild = (ImageView) findViewById(R.id.imgAddChild);
        imgPic = (CircularImageView) findViewById(R.id.imgPic);
        linChildrenDetailContainer = (LinearLayout) findViewById(R.id.linChildrenDetailContainer);
        if (FMCApplication.loggedinUserPic != null) {
            imgPic.setImageBitmap(FMCApplication.loggedinUserPic);
        }else{

            new GetProfilePic().execute(AccessToken.getCurrentAccessToken().getUserId());
        }
        adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linChildrenDetailContainer != null) {
                    Log.d(TAG,"Total number of children present is = "+linChildrenDetailContainer.getChildCount());
                }

                if(checkForRequiredField()){
                    startNextActivity();
                }
                //startNextActivity();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity();
            }
        });
        imgAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout childView = (LinearLayout) layoutInflater.inflate(R.layout.children_detail,linChildrenDetailContainer,false);
                EditText edtAge = (EditText) childView.findViewById(R.id.edtAge);
                Spinner spinnerGender = (Spinner) childView.findViewById(R.id.spinnerGender);
                spinnerGender.setAdapter(adapter);
                linChildrenDetailContainer.addView(childView);

            }
        });

    }

    private void startNextActivity(){
        Intent intent = new Intent(EditProfileActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean checkForRequiredField(){
        boolean result = true;
        UserData userData = new UserData();

        if(TextUtils.isEmpty(txtBirthday.getText())){
            //txtBirthday.setError("This field cannot be empty");
            showErrorDialog(getString(R.string.please_enter_birthday),getString(R.string.error));
            return false;
        }else{
            userData.setBirthDay(txtBirthday.getText().toString());
        }
        if(TextUtils.isEmpty(edtBitchUserName.getText())){
            result = false;
            edtBitchUserName.setError(getString(R.string.field_cannot_be_empty));
            return false;
            //showErrorDialog(getString(R.string.please_enter_username),getString(R.string.error));
        }else{
            userData.setBitchUserName(edtBitchUserName.getText().toString());
        }
        if(TextUtils.isEmpty(edtBitchPassword.getText())){
            result = false;
            edtBitchPassword.setError(getString(R.string.field_cannot_be_empty));
            return false;
            //showErrorDialog(getString(R.string.please_enter_password),getString(R.string.error));
        }else{
            userData.setBitchPassword(edtBitchPassword.getText().toString());
        }


        if(linChildrenDetailContainer != null){
            for(int i = 0; i < linChildrenDetailContainer.getChildCount(); i ++){
                ChildData childData = new ChildData();
                LinearLayout childView = (LinearLayout) linChildrenDetailContainer.getChildAt(i);
                EditText edtAge = (EditText) childView.findViewById(R.id.edtAge);
                Spinner spinnerGender = (Spinner) childView.findViewById(R.id.spinnerGender);

                if(TextUtils.isEmpty(edtAge.getText())){
                    result = false;
                    edtAge.setError(getString(R.string.field_cannot_be_empty));
                    //showErrorDialog(getString(R.string.please_enter_child_age),getString(R.string.error));
                    return false;
                }else{
                    childData.setAge(Integer.parseInt(edtAge.getText().toString()));
                }
                childData.setGender(spinnerGender.getSelectedItem().toString());
                userData.getArrChildData().add(childData);
                //Log.d(TAG,spinnerGender.getSelectedItem().toString());
            }
        }

        if(result){
            FMCApplication.loggedinUserData = userData;
            FMCApplication.mPreffs.edit().putBoolean(Constants.PREFS_PROFILE_SET,true).apply();
        }

        return result;
    }

    class GetProfilePic extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub

            return CommonMethods.getFacebookProfilePicture(EditProfileActivity.this, params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result != null && imgPic != null){
                imgPic.setImageBitmap(result);
                FMCApplication.loggedinUserPic = result;
            }
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

}
