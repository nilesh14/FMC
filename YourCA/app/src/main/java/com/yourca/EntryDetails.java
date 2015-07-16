package com.yourca;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yourca.constants.CommonMethods;
import com.yourca.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Nilesh on 03/07/15.
 */
public class EntryDetails extends Activity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    private static final String TAG = "EntryDetails";
    DatePickerDialog d;
    Spinner spinnerTransaction;
    EditText edtAmount,edtDesc;
    TextView txtDatePicker,txtTimePicker;
    ImageView imgImage;
    Button btnDone;
    ProgressDialog pDialog;
    private static final int CAMERA_REQUEST = 1022;


    ArrayAdapter<CharSequence> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_details_activity);

        adapter = ArrayAdapter.createFromResource(this,R.array.transaction_spinner_val,android.R.layout.simple_spinner_item);


        spinnerTransaction = (Spinner) findViewById(R.id.spinnerTransaction);
        edtAmount = (EditText) findViewById(R.id.edtAmount);
        edtDesc = (EditText) findViewById(R.id.edtDesc);
        txtDatePicker = (TextView) findViewById(R.id.txtDatePicker);
        txtTimePicker = (TextView) findViewById(R.id.txtTimePicker);
        imgImage = (ImageView) findViewById(R.id.imgImage);
        spinnerTransaction.setAdapter(adapter);
        btnDone = (Button) findViewById(R.id.btnDone);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        txtDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        EntryDetails.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        txtTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        EntryDetails.this,
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
            }
        });

        imgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);*/

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File f = new File(android.os.Environment.getExternalStorageDirectory(), "YourCA.jpg");

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                //pic = f;

                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateText = txtDatePicker.getText().toString();

                if(TextUtils.isEmpty(edtAmount.getText())){
                    edtAmount.setError(getString(R.string.enter_amount));
                }else if(dateText.equalsIgnoreCase(getString(R.string.select_date_time))){
                    showErrorDialog("Error","Please set date and time");
                    //txtDatePicker.setError("Please set date and time");
                }
                else{
                    HashMap<String,String> data = new HashMap<String, String>();
                    data.put("id",YourCAApplication.mPrefs.getString(Constants.PREFS_ID,""));
                    data.put("type",(String)spinnerTransaction.getSelectedItem());
                    data.put("amount",edtAmount.getText().toString());
                    data.put("dt",txtDatePicker.getText().toString());
                    data.put("desc",(TextUtils.isEmpty(edtDesc.getText())?"":edtDesc.getText().toString()));
                    new LoginTask(data,EntryDetails.this).execute(Constants.ADD_ENTRY_URL);
                }
            }
        });

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

            pDialog.setMessage(getString(R.string.save_details));
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
                        String id = jsonObject.optString("success");
                        if(!TextUtils.isEmpty(id) && id.equalsIgnoreCase("1")){
                            Toast.makeText(EntryDetails.this,"Details Saved",Toast.LENGTH_SHORT).show();
                            edtAmount.setText("");
                            edtDesc.setText("");
                            txtDatePicker.setText("");
                            imgImage.setImageResource(R.drawable.camera);
                        }else{
                            showErrorDialog("Error","Error while saving details. Please try again");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                showErrorDialog("Error","Error while saving details. Please try again");
            }

        }
    }

    private void showErrorDialog(String title,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(EntryDetails.this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK",null);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            /*Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgImage.setImageBitmap(photo);*/

            File f = new File(Environment.getExternalStorageDirectory().toString());

            for (File temp : f.listFiles()) {

                if (temp.getName().equals("YourCA.jpg")) {

                    f = temp;
                    File photo = new File(Environment.getExternalStorageDirectory(), "YourCA.jpg");
                    //pic = photo;
                    Bitmap bitmap;

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),

                            bitmapOptions);



                    imgImage.setImageBitmap(bitmap);

                    break;

                }

            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+monthOfYear+"/"+year;
        txtDatePicker.setText(date);
        showTimePicker();
    }

    private void showTimePicker(){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                EntryDetails.this,
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
                txtDatePicker.setText("");
            }
        });

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Log.d(TAG,"hourofDay = "+hourOfDay+ " minute =  "+minute);
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String time = hourString+" : "+minuteString;
        String text = txtDatePicker.getText().toString();
        if(!TextUtils.isEmpty(text)){
            txtDatePicker.setText(text+" "+time);
        }else{
            txtDatePicker.setText("");
        }

    }
}
