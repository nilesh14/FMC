package v1.pyroteck.com.pyroteck;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import v1.pyroteck.com.pyroteck.application.PyrotekApplication;
import v1.pyroteck.com.pyroteck.callbacks.DescriptionCallBack;
import v1.pyroteck.com.pyroteck.constants.CommonMethods;
import v1.pyroteck.com.pyroteck.constants.Constants;
import v1.pyroteck.com.pyroteck.data.SizeData;
import v1.pyroteck.com.pyroteck.data.SubCategory;

/**
 * Created by Nilesh on 01/05/15.
 */
public class DescriptionFragment extends Fragment{

    private static final String TAG = "DescriptionFragment";
    TextView txtMaterialData,txtDescriptionData,txtTitle;
    //Button btnSendEnquiry;
    ImageView imgProductImage;
    LinearLayout linSizeStatic,linSizeTable,linAvailableForms,linCall,linEmail,linShare;

    PyrotekApplication app;
    SharedPreferences mPrefs;
    TextView txtSize,txtSizeValue,txtAvailableFormsValue;

    LayoutInflater inflater;
    HashMap<String,String> mapData;
    DescriptionCallBack mCallBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.description_layout,container,false);

        this.inflater = LayoutInflater.from(getActivity());
        txtDescriptionData = (TextView) view.findViewById(R.id.txtDescriptionData);
        txtMaterialData = (TextView) view.findViewById(R.id.txtMaterialData);

        app = (PyrotekApplication) getActivity().getApplication();
        mPrefs = app.getmPrefs();
        mapData = new HashMap<>();
        //btnSendEnquiry = (Button) view.findViewById(R.id.btnSendEnquiry);
        imgProductImage = (ImageView) view.findViewById(R.id.imgProductImage);
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);

        linCall = (LinearLayout) view.findViewById(R.id.linCall);
        linEmail = (LinearLayout) view.findViewById(R.id.linEmail);
        linShare = (LinearLayout) view.findViewById(R.id.linShare);

        linSizeStatic = (LinearLayout) view.findViewById(R.id.linSizeStatic);
        linSizeTable = (LinearLayout) view.findViewById(R.id.linSizeTable);

        txtSize = (TextView) view.findViewById(R.id.txtSize);
        txtSizeValue = (TextView) view.findViewById(R.id.txtSizeValue);
        linAvailableForms = (LinearLayout) view.findViewById(R.id.linAvailableForms);
        txtAvailableFormsValue = (TextView) view.findViewById(R.id.txtAvailableFormsValue);

        Bundle bundle = getArguments();

        final SubCategory subCategory = (SubCategory) bundle.getSerializable("dataSubcategory");

        if(subCategory != null){
            /*mapData.clear();
            mapData.put("uid", String.valueOf(mPrefs.getInt(PyrotekApplication.UID, 0)));
            mapData.put("product",subCategory.getTitle());*/
            mCallBack.sendUsageRequest(subCategory);
            //new RegisterUsageEvent(mapData).execute(Constants.REGISTER_USAGE_EVENT_URL);
            txtMaterialData.setText(subCategory.getMaterial());
            txtDescriptionData.setText(subCategory.getDescription());
            imgProductImage.setImageResource(subCategory.getImageID());
            txtTitle.setText(subCategory.getTitle());

            if(subCategory.availableColors != null && subCategory.availableForms != null){
                linSizeTable.setVisibility(View.GONE);
                linSizeStatic.setVisibility(View.VISIBLE);
                linAvailableForms.setVisibility(View.VISIBLE);

                txtSize.setText(getResources().getString(R.string.available_forms));
                txtSizeValue.setText(subCategory.availableForms);

                txtAvailableFormsValue.setText(subCategory.availableColors);
            }else{
                prepareTable(subCategory);
            }

        }

        linCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + "+919860606609"));
                startActivity(intent);
            }
        });

        linEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail("Enquiry about " + subCategory.getTitle(), "Need more info about the product: " + subCategory.getTitle());
            }
        });

        linShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share("Enquiry about " + subCategory.getTitle(), "Need more info about the product: " + subCategory.getTitle());
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallBack = (DescriptionCallBack) activity;
    }

    class RegisterUsageEvent extends AsyncTask<String,Void,String> {

        private static final String TAG = "RegisterNewUser";

        HashMap<String,String> data;

        public RegisterUsageEvent(HashMap<String,String> data) {
            this.data = data;
        }


        @Override
        protected String doInBackground(String... params) {
            return CommonMethods.callSyncService(getActivity(), data, params[0], TAG);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        int code = jObj.optInt("code");
                        if(code == 1){
                            Log.d(TAG,"Event Sent");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    protected void sendEmail(String subject,String body) {
        Log.i("Send email", "");

        String[] TO = {"safety_clothing@pyrotek-inc.com"};
        //String[] CC = {"mcmohd@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            //finish();
            Log.d(TAG, "Finished sending email...");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(),
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void share(String subject,String body){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
       // String shareBody = "Here is the share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void prepareTable(SubCategory subCategory){
        if(subCategory.isShowTable()){
            linSizeTable.setVisibility(View.VISIBLE);
            linSizeStatic.setVisibility(View.GONE);

            ArrayList<SizeData> arrSizeData = subCategory.getArrSizeData();

            if(arrSizeData != null && arrSizeData.size() > 0){

                for (SizeData sizeData : arrSizeData) {

                    View view = inflater.inflate(R.layout.size_cell_layout,linSizeTable,false);
                    TextView txtSize,txtMetricCm;
                    txtSize = (TextView) view.findViewById(R.id.txtSize);
                    txtMetricCm = (TextView) view.findViewById(R.id.txtMetricCm);

                    txtSize.setText(sizeData.getSize());

                    if(subCategory.isCheckCustomeSize()){
                        txtMetricCm.setText(sizeData.getCustomeSizes());
                    }else{
                        txtMetricCm.setText(String.valueOf(sizeData.getMetricCM()));
                    }

                    linSizeTable.addView(view);
                }

            }
        }else{
            linSizeTable.setVisibility(View.GONE);
            linSizeStatic.setVisibility(View.VISIBLE);

            ArrayList<SizeData> arrSizeData = subCategory.getArrSizeData();

            if(arrSizeData != null && arrSizeData.size() > 0) {
                StringBuilder data = new StringBuilder();
                for(int i = 0; i < arrSizeData.size(); i++){
                    SizeData sizeData = arrSizeData.get(i);
                    data.append(sizeData.getSize());
                    if(i != (arrSizeData.size() - 1)){
                        data.append(",");
                    }
                }

                txtSizeValue.setText(data.toString());
            }

        }
    }
}
