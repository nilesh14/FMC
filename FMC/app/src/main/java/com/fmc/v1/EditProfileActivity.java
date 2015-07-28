package com.fmc.v1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.constants.CommonMethods;
import com.fmc.v1.constants.Constants;
import com.fmc.v1.data.ChildData;
import com.fmc.v1.data.HashtagData;
import com.fmc.v1.data.UserData;
import com.fmc.v1.dialog.HashTagDialog;
import com.fmc.v1.view.CircularImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Nilesh on 27/06/15.
 */
public class EditProfileActivity extends Activity implements android.app.DatePickerDialog.OnDateSetListener {

    private static final String TAG = "EditProfileActivity";
    Button btnDone, btnCancel;

    EditText  edtBio, edtWebsite, edtBitchUserName, edtBitchPassword;
    TextView txtHashTag;
    TextView txtBirthday;

    ImageView imgAddChild;
    CircularImageView imgPic;
    LinearLayout linChildrenDetailContainer;
    LayoutInflater layoutInflater;
    ArrayAdapter<CharSequence> adapter;
    TextView txtName, txtPrivateInformation, edtChildrenInfo;
    UserData userData = new UserData();
    ArrayList<HashtagData> arrData = new ArrayList<HashtagData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setTypeface(FMCApplication.ubuntu);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setTypeface(FMCApplication.ubuntu);
        txtPrivateInformation = (TextView) findViewById(R.id.txtPrivateInformation);
        txtPrivateInformation.setTypeface(FMCApplication.ubuntu);
        edtChildrenInfo = (TextView) findViewById(R.id.edtChildrenInfo);
        edtChildrenInfo.setTypeface(FMCApplication.ubuntu);

        txtHashTag = (TextView) findViewById(R.id.txtHashtag);
        txtHashTag.setTypeface(FMCApplication.ubuntu);
        edtBio = (EditText) findViewById(R.id.edtBio);
        edtBio.setTypeface(FMCApplication.ubuntu);
        edtWebsite = (EditText) findViewById(R.id.edtWebsite);
        edtWebsite.setTypeface(FMCApplication.ubuntu);
        edtBitchUserName = (EditText) findViewById(R.id.edtBitchUserName);
        edtBitchUserName.setTypeface(FMCApplication.ubuntu);
        edtBitchPassword = (EditText) findViewById(R.id.edtBitchPassword);
        edtBitchPassword.setTypeface(FMCApplication.ubuntu);
        txtBirthday = (TextView) findViewById(R.id.txtBirthday);
        txtBirthday.setTypeface(FMCApplication.ubuntu);
        txtName = (TextView) findViewById(R.id.txtName);
        txtName.setTypeface(FMCApplication.ubuntu);
        layoutInflater = LayoutInflater.from(this);
        String[] dataAge = getResources().getStringArray(R.array.gender_array);

        /*if(FMCApplication.mPreffs.getBoolean(Constants.PREFS_PROFILE_SET,false)){
            startNextActivity();
        }*/

        userData.setName(FMCApplication.mPreffs.getString(Constants.PREFS_USER_NAME, ""));

        txtName.setText(FMCApplication.mPreffs.getString(Constants.PREFS_USER_NAME, ""));

        imgAddChild = (ImageView) findViewById(R.id.imgAddChild);
        imgPic = (CircularImageView) findViewById(R.id.imgPic);
        linChildrenDetailContainer = (LinearLayout) findViewById(R.id.linChildrenDetailContainer);
        /*if (FMCApplication.loggedinUserPic != null) {
            imgPic.setImageBitmap(FMCApplication.loggedinUserPic);
        } else {

            new GetProfilePic().execute(AccessToken.getCurrentAccessToken().getUserId());*/
        FMCApplication app = (FMCApplication) getApplication();
        String url = app.makeFacebookProfileURL(AccessToken.getCurrentAccessToken().getUserId());
        ImageLoader.getInstance().displayImage(url, imgPic, FMCApplication.options);
        //}
        adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);

        //adapter = new MyArrayAdapter(EditProfileActivity.this,android.R.layout.simple_spinner_item,dataAge);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.dropdown_resource);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linChildrenDetailContainer != null) {
                    Log.d(TAG, "Total number of children present is = " + linChildrenDetailContainer.getChildCount());
                }

                checkForRequiredField();
                // if (checkForRequiredField()) {
                startNextActivity();
                // }
                //startNextActivity();

            }
        });

        txtHashTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashTagDialog dialog = new HashTagDialog(EditProfileActivity.this,R.style.CommentDialogtheme);
                dialog.setOwnerActivity(EditProfileActivity.this);
                prepareHashtagData();
                dialog.setArrData(arrData);
                txtHashTag.setText("");
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (arrData != null) {
                            StringBuilder hashTagText = new StringBuilder();
                            int i = 0;
                            for (HashtagData hashtagData : arrData) {
                                if(hashtagData != null){
                                    if(hashtagData.isSelected()){
                                        if(i > 0){
                                           hashTagText.append(" , ");
                                        }
                                        hashTagText.append(hashtagData.getText());
                                        i++;
                                        //hashTagText.append(",");

                                    }
                                }
                            }

                            txtHashTag.setText(""+hashTagText.toString());
                        }
                    }
                });
                dialog.show();
            }
        });

        txtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                // DatePickerDialog dpd = DatePickerDialog.newInstance(EditProfileActivity.this,now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
                android.app.DatePickerDialog dp = new android.app.DatePickerDialog(EditProfileActivity.this, EditProfileActivity.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                dp.show();
                /*DatePickerDialog dpd = DatePickerDialog.newInstance(EditProfileActivity.this,now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
                dpd.show(getFragmentManager(),"DatePickerDialog");*/

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
                LinearLayout childView = (LinearLayout) layoutInflater.inflate(R.layout.children_detail, linChildrenDetailContainer, false);
                final EditText edtAge = (EditText) childView.findViewById(R.id.edtAge);
                edtAge.setTypeface(FMCApplication.ubuntu);
                final ImageView imgChildImage = (ImageView) childView.findViewById(R.id.imgChildImage);
                final Spinner spinnerGender = (Spinner) childView.findViewById(R.id.spinnerGender);
                spinnerGender.setAdapter(adapter);

                spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(!TextUtils.isEmpty(edtAge.getText())){
                            int age = Integer.parseInt(edtAge.getText().toString());
                            setChildImage(position,age,imgChildImage);
                        }else {
                            setDefaultChildImage(position,imgChildImage);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                edtAge.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(!TextUtils.isEmpty(s)){
                            int age = Integer.parseInt(s.toString());
                            setChildImage(spinnerGender.getSelectedItemPosition(),age,imgChildImage);
                            /*if(spinnerGender.getSelectedItemPosition() == 0){
                                if(age > 0 && age <= 2){
                                    imgChildImage.setImageResource(R.drawable.male_1_2_years);
                                }else if(age >= 3 && age <= 4){
                                    imgChildImage.setImageResource(R.drawable.male_3_4_years);
                                }else{
                                    imgChildImage.setImageResource(R.drawable.male_5_10_years);
                                }
                            }else{
                                if(age > 0 && age <= 2){
                                    imgChildImage.setImageResource(R.drawable.female_1_2_years);
                                }else if(age >= 3 && age <= 4){
                                    imgChildImage.setImageResource(R.drawable.female_3_4_years);
                                }else{
                                    imgChildImage.setImageResource(R.drawable.female_5_10_years);
                                }
                            }*/
                        }else{
                            /*if(spinnerGender.getSelectedItemPosition() == 0){
                                imgChildImage.setImageResource(R.drawable.male_1_2_years);
                            }else{
                                imgChildImage.setImageResource(R.drawable.female_1_2_years);
                            }*/
                            setDefaultChildImage(spinnerGender.getSelectedItemPosition(),imgChildImage);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                linChildrenDetailContainer.addView(childView);

            }
        });

        prepareInitialData();

    }

    private void prepareHashtagData(){
        arrData.clear();
        for(int i = 0; i < 6; i ++){
            HashtagData data = new HashtagData();
            data.setText("#Tag Number "+i);
            arrData.add(data);
        }
    }

    private void setDefaultChildImage(int seletedGender,ImageView imgChildImage){
        if(seletedGender == 0){
            imgChildImage.setImageResource(R.drawable.male_1_2_years);
        }else{
            imgChildImage.setImageResource(R.drawable.female_1_2_years);
        }
    }

    private void setChildImage(int selectedGender,int age,ImageView imgChildImage){
        //int age = Integer.parseInt(s.toString());
        if(selectedGender == 0){
            if(age > 0 && age <= 2){
                imgChildImage.setImageResource(R.drawable.male_1_2_years);
            }else if(age >= 3 && age <= 4){
                imgChildImage.setImageResource(R.drawable.male_3_4_years);
            }else{
                imgChildImage.setImageResource(R.drawable.male_5_10_years);
            }
        }else{
            if(age > 0 && age <= 2){
                imgChildImage.setImageResource(R.drawable.female_1_2_years);
            }else if(age >= 3 && age <= 4){
                imgChildImage.setImageResource(R.drawable.female_3_4_years);
            }else{
                imgChildImage.setImageResource(R.drawable.female_5_10_years);
            }
        }
    }

    private class MyArrayAdapter extends ArrayAdapter<String> {

        String[] data;

        public MyArrayAdapter(Context context, int textViewResourceId, String[] data) {
            super(context, textViewResourceId, data);
        }

        public TextView getView(int position, View convertView, ViewGroup parent) {
            TextView v = (TextView) super.getView(position, convertView, parent);
            v.setTypeface(FMCApplication.ubuntu);
            v.setText(getItem(position));
            return v;
        }

        public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView v = (TextView) super.getView(position, convertView, parent);
            v.setTypeface(FMCApplication.ubuntu);
            v.setTextSize(20);
            v.setText(getItem(position));

            return v;
        }

    }

    private void prepareInitialData() {
        edtBio.setText(FMCApplication.mPreffs.getString(Constants.PREFS_BIO, ""));
        edtWebsite.setText(FMCApplication.mPreffs.getString(Constants.PREFS_WEBSITE, ""));
        txtHashTag.setText(FMCApplication.mPreffs.getString(Constants.PREFS_HASHTAGS, ""));
        txtBirthday.setText(FMCApplication.mPreffs.getString(Constants.PREFS_BIRTHDAY, ""));
        edtBitchUserName.setText(FMCApplication.mPreffs.getString(Constants.PREFS_BITCH_USERNAME, ""));

        Gson gson = new Gson();
        Type type = new TypeToken<List<ChildData>>() {
        }.getType();
        ArrayList<ChildData> arrChildData = gson.fromJson(FMCApplication.mPreffs.getString(Constants.PREFS_CHILD_DETAIL, ""), type);
        if (arrChildData != null) {
            Log.d(TAG, "Child found = " + arrChildData.size());

            for (ChildData childData : arrChildData) {
                LinearLayout childView = (LinearLayout) layoutInflater.inflate(R.layout.children_detail, linChildrenDetailContainer, false);
                final EditText edtAge = (EditText) childView.findViewById(R.id.edtAge);
                edtAge.setTypeface(FMCApplication.ubuntu);
                final Spinner spinnerGender = (Spinner) childView.findViewById(R.id.spinnerGender);
                spinnerGender.setAdapter(adapter);
                final ImageView imgChildImage = (ImageView) childView.findViewById(R.id.imgChildImage);

                TextView spinnerText = (TextView) spinnerGender.findViewById(android.R.id.text1);
                if (spinnerText != null) {
                    spinnerText.setTypeface(FMCApplication.ubuntu);
                }

                edtAge.setText(String.valueOf(childData.getAge()));
                if (childData.getGender().equalsIgnoreCase("MALE")) {
                    spinnerGender.setSelection(0);
                } else {
                    spinnerGender.setSelection(1);
                }


                spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(!TextUtils.isEmpty(edtAge.getText())){
                            int age = Integer.parseInt(edtAge.getText().toString());
                            setChildImage(position,age,imgChildImage);
                        }else {
                            setDefaultChildImage(position,imgChildImage);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                edtAge.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!TextUtils.isEmpty(s)) {
                            int age = Integer.parseInt(s.toString());
                            setChildImage(spinnerGender.getSelectedItemPosition(), age, imgChildImage);
                            /*if(spinnerGender.getSelectedItemPosition() == 0){
                                if(age > 0 && age <= 2){
                                    imgChildImage.setImageResource(R.drawable.male_1_2_years);
                                }else if(age >= 3 && age <= 4){
                                    imgChildImage.setImageResource(R.drawable.male_3_4_years);
                                }else{
                                    imgChildImage.setImageResource(R.drawable.male_5_10_years);
                                }
                            }else{
                                if(age > 0 && age <= 2){
                                    imgChildImage.setImageResource(R.drawable.female_1_2_years);
                                }else if(age >= 3 && age <= 4){
                                    imgChildImage.setImageResource(R.drawable.female_3_4_years);
                                }else{
                                    imgChildImage.setImageResource(R.drawable.female_5_10_years);
                                }
                            }*/
                        } else {
                            /*if(spinnerGender.getSelectedItemPosition() == 0){
                                imgChildImage.setImageResource(R.drawable.male_1_2_years);
                            }else{
                                imgChildImage.setImageResource(R.drawable.female_1_2_years);
                            }*/
                            setDefaultChildImage(spinnerGender.getSelectedItemPosition(), imgChildImage);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                //spinnerGender.setse
                linChildrenDetailContainer.addView(childView);
            }

        } else {
            Log.d(TAG, "No Child found");
        }
    }

    private void startNextActivity() {
        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
        intent.putExtra("show_profile", true);
        FMCApplication.mPreffs.edit().putBoolean(Constants.PREFS_PROFILE_SET, true).apply();
        startActivity(intent);
        finish();
    }

    private void saveUserData() {
        SharedPreferences.Editor mPreffsEditor = FMCApplication.mPreffs.edit();

    }

    // All Compulsory checks removed
    private boolean checkForRequiredField() {
        boolean result = true;
        SharedPreferences.Editor mPreffsEditor = FMCApplication.mPreffs.edit();

        if (TextUtils.isEmpty(txtBirthday.getText())) {
            //txtBirthday.setError("This field cannot be empty");
            //showErrorDialog(getString(R.string.please_enter_birthday), getString(R.string.error));
            //return false;
        } else {
            mPreffsEditor.putString(Constants.PREFS_BIRTHDAY, txtBirthday.getText().toString());
            userData.setBirthDay(txtBirthday.getText().toString());
        }
        if (edtBitchUserName != null) {
            userData.setBitchUserName(edtBitchUserName.getText().toString());
            mPreffsEditor.putString(Constants.PREFS_BITCH_USERNAME, edtBitchUserName.getText().toString());
        }

        if (TextUtils.isEmpty(edtBitchPassword.getText())) {
            result = false;
            //edtBitchPassword.setError(getString(R.string.field_cannot_be_empty));
            //return false;
            //showErrorDialog(getString(R.string.please_enter_password),getString(R.string.error));
        } else {
            userData.setBitchPassword(edtBitchPassword.getText().toString());
            mPreffsEditor.putString(Constants.PREFS_BITCH_PASSWORD, edtBitchPassword.getText().toString());
        }

        if (!TextUtils.isEmpty(txtHashTag.getText())) {
            userData.setHashtags(txtHashTag.getText().toString());
            mPreffsEditor.putString(Constants.PREFS_HASHTAGS, txtHashTag.getText().toString());
        }else{
            userData.setHashtags(txtHashTag.getText().toString());
            mPreffsEditor.putString(Constants.PREFS_HASHTAGS, "--");
        }

        if (!TextUtils.isEmpty(edtBio.getText())) {
            userData.setBio(edtBio.getText().toString());
            mPreffsEditor.putString(Constants.PREFS_BIO, edtBio.getText().toString());
        }else{
            userData.setBio(edtBio.getText().toString());
            mPreffsEditor.putString(Constants.PREFS_BIO, "--");
        }

        if (!TextUtils.isEmpty(edtWebsite.getText())) {
            userData.setWebsite(edtWebsite.getText().toString());
            mPreffsEditor.putString(Constants.PREFS_WEBSITE, edtWebsite.getText().toString());
        }else{
            userData.setWebsite(edtWebsite.getText().toString());
            mPreffsEditor.putString(Constants.PREFS_WEBSITE, "---");
        }


        if (linChildrenDetailContainer != null) {
            for (int i = 0; i < linChildrenDetailContainer.getChildCount(); i++) {
                ChildData childData = new ChildData();
                LinearLayout childView = (LinearLayout) linChildrenDetailContainer.getChildAt(i);
                EditText edtAge = (EditText) childView.findViewById(R.id.edtAge);
                edtAge.setTypeface(FMCApplication.ubuntu);
                Spinner spinnerGender = (Spinner) childView.findViewById(R.id.spinnerGender);

                TextView spinnerText = (TextView) spinnerGender.findViewById(android.R.id.text1);
                if (spinnerText != null) {
                    spinnerText.setTypeface(FMCApplication.ubuntu);
                }

                if (TextUtils.isEmpty(edtAge.getText())) {
                    result = false;
                    //edtAge.setError(getString(R.string.field_cannot_be_empty));
                    //showErrorDialog(getString(R.string.please_enter_child_age),getString(R.string.error));
                    //return false;
                } else {

                    String regexStr = "^[0-9]*$";

                    if (edtAge.getText().toString().trim().matches(regexStr)) {
                        childData.setAge(Integer.parseInt(edtAge.getText().toString().trim()));
                    } else {
                        edtAge.setError("Not a Valid Age");
                    }

                }
                childData.setGender(spinnerGender.getSelectedItem().toString());
                if (childData.getAge() != 0) {

                    userData.getArrChildData().add(childData);
                }


                //Log.d(TAG,spinnerGender.getSelectedItem().toString());
            }

            Gson gson = new Gson();
            String childJsonObject = gson.toJson(userData.getArrChildData());
            Log.d(TAG, "Json Child Data " + childJsonObject);
            mPreffsEditor.putString(Constants.PREFS_CHILD_DETAIL, childJsonObject).apply();
        }

        if (result) {
            FMCApplication.loggedinUserData = userData;
            FMCApplication.mPreffs.edit().putBoolean(Constants.PREFS_PROFILE_SET, true).apply();
        }

        return result;
    }

    /*@Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {

        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        if((currentYear - year) >= 16){
            String date = dayOfMonth+"/"+monthOfYear+"/"+year;
            txtBirthday.setText(date);
            showTimePicker();
        }else{
            Toast.makeText(EditProfileActivity.this,"Age should be greater than 16",Toast.LENGTH_SHORT).show();
        }


    }*/

    private void showTimePicker() {
        Calendar now = Calendar.getInstance();
        /*TimePickerDialog tpd = TimePickerDialog.newInstance(
                EditProfileActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        //tpd.setThemeDark(modeDarkTime.isChecked());
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
            }
        });
        tpd.show(getFragmentManager(), "Timepickerdialog");

        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                txtBirthday.setText("");
            }
        });*/

    }

   /* @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
        Log.d(TAG,"hourofDay = "+hourOfDay+ " minute =  "+minute);
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String time = hourString+" : "+minuteString;
        String text = txtBirthday.getText().toString();
        if(!TextUtils.isEmpty(text)){
            txtBirthday.setText(text+" "+time);
        }else{
            txtBirthday.setText("");
        }
    }*/

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        if ((currentYear - year) >= 16) {
            String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            txtBirthday.setText(date);
            //showTimePicker();
        } else {
            Toast.makeText(EditProfileActivity.this, "Age should be greater than 16", Toast.LENGTH_SHORT).show();
        }
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
            if (result != null && imgPic != null) {
                imgPic.setImageBitmap(result);
                FMCApplication.loggedinUserPic = result;
            }
        }

    }

    private void showErrorDialog(String msg, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title == null ? "Alert" : title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}
