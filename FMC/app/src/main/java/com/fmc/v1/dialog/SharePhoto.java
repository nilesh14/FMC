package com.fmc.v1.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fmc.v1.R;
import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.connection.AndroidMultiPartEntity;
import com.fmc.v1.constants.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by Nilesh on 25/07/15.
 */
public class SharePhoto extends Dialog {


    private static final String TAG = "SharePhoto";
    EditText edtComment;
    ImageView imgImage;
    Button btnSend;
    Bitmap image;
    Toast toast;
    String filePath;
    long totalSize = 0;
    ProgressDialog pDialog;

    public SharePhoto(Context context) {
        super(context);
    }

    public SharePhoto(Context context, int theme) {
        super(context, theme);
    }

    protected SharePhoto(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.share_photo_dialog);

        toast = Toast.makeText(getOwnerActivity(),"",Toast.LENGTH_SHORT);
        pDialog = new ProgressDialog(getOwnerActivity());
        pDialog.setCancelable(false);

        edtComment = (EditText) findViewById(R.id.edtComment);
        imgImage = (ImageView) findViewById(R.id.imgImage);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getOwnerActivity(), "Image will be sent...", Toast.LENGTH_SHORT).show();
                new UploadFileToServer().execute();
                //dismiss();
            }
        });

        if (image != null) {
            imgImage.setImageBitmap(image);
        }

    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            //progressBar.setProgress(0);
            super.onPreExecute();

            pDialog.setMessage("Uploading " + 0 + "% ...");
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            //progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            //progressBar.setProgress(progress[0]);

            // updating percentage value
            //toast.set
            pDialog.setMessage("Uploading " + String.valueOf(progress[0]) + "% ...");
            //toast.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constants.ADD_POST__URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("filename", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server  "uid", String.valueOf(FMCApplication.mPreffs.getInt(Constants.PREFS_UID, 0))
                entity.addPart("uid",
                        new StringBody(String.valueOf(FMCApplication.mPreffs.getInt(Constants.PREFS_UID, 0))));
                //  "post", wallData.getTextPost()
                if(!TextUtils.isEmpty(edtComment.getText())){
                    entity.addPart("post", new StringBody(edtComment.getText().toString()));

                }

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            // showing the server response in an alert dialog
            // Response from server: [{"code":1}]
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        String code = jsonObject.optString("code");
                        if(!TextUtils.isEmpty(code)){
                            if(code.equalsIgnoreCase("1")){
                                showAlert("Done");
                            }else {
                                showAlert("Something went wrong, Please try again...");
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                showAlert("Something went wrong, Please try again...");
            }
            //showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getOwnerActivity());
        builder.setMessage(message).setTitle("Message")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                        dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
