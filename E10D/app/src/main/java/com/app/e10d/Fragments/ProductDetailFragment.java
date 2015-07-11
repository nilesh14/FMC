package com.app.e10d.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.e10d.Data.ProductData;
import com.app.e10d.Interfaces.GeneralCallbacks;
import com.app.e10d.R;
import com.app.e10d.application.E10DApplication;
import com.app.e10d.constants.CommonMethods;
import com.app.e10d.constants.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Nilesh on 09/07/15.
 */
public class ProductDetailFragment extends Fragment {

    private static final String TAG = "ProductDetailFragment" ;
    TextView txtProductName,txtProductPrice,txtProductDetails,txtItemCount;
    ImageView imgProductImage,imgMinus,imgPlus;
    Button btnAddToCart;
    ProgressDialog pDialog;
    GeneralCallbacks mGeneralCallBacks;
    List<ProductData> arrProductData;
    DisplayImageOptions options;
    int pid;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_detail_fragment,container,false);
        txtProductName = (TextView) view.findViewById(R.id.txtProductName);
        txtProductPrice = (TextView) view.findViewById(R.id.txtProductPrice);
        txtProductDetails = (TextView) view.findViewById(R.id.txtProductDetails);
        txtItemCount = (TextView) view.findViewById(R.id.txtItemCount);
        imgMinus = (ImageView) view.findViewById(R.id.imgMinus);
        imgPlus = (ImageView) view.findViewById(R.id.imgPlus);
        imgProductImage = (ImageView) view.findViewById(R.id.imgProductImage);
        btnAddToCart = (Button) view.findViewById(R.id.btnAddToCart);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> data = new HashMap<String, String>();
                data.put("uid", E10DApplication.mPrefs.getString(Constants.PREFS_ID,""));
                data.put("pid", String.valueOf(pid));
                data.put("qty", txtItemCount.getText().toString());
                new AddToCartTask(data,getActivity(),mGeneralCallBacks).execute(Constants.ADD_TO_CART_URL);
            }
        });

        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int value =  Integer.parseInt(txtItemCount.getText().toString());
                value --;
                if(value > 0){
                    txtItemCount.setText(String.valueOf(value));
                }
            }
        });

        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value =  Integer.parseInt(txtItemCount.getText().toString());
                if(value > 0){
                    value ++;
                    txtItemCount.setText(String.valueOf(value));
                }
            }
        });

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_placeholder)
                .showImageForEmptyUri(R.drawable.empty_placeholder)
                .showImageOnFail(R.drawable.empty_placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        pid = getArguments().getInt("tag");
        HashMap<String,String> data = new HashMap<String,String>();
        data.put("pid",String.valueOf(pid));
        new GetProductsTask(data,getActivity()).execute(Constants.PRODUCTS_DETAIL_URL);

        return view;
    }


    class GetProductsTask extends AsyncTask<String,Void,String> {

        HashMap<String,String> data;
        Context context;
        GetProductsTask(HashMap<String, String> data, Context context){
            this.data = data;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (pDialog == null) {
                pDialog = new ProgressDialog(context);
            }

            pDialog.setMessage(getString(R.string.fetching_products_detail));
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if(data == null || data.size() == 0){
                return CommonMethods.callSyncService(this.context, params[0], TAG);
            }else{

                return CommonMethods.callSyncService(this.context,data,  params[0], TAG);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(pDialog != null && pDialog.isShowing()){
                pDialog.dismiss();
            }

            try {
                if (s != null) {
                    Gson gson = new Gson();
                    //arrProductData = gson.fromJson(s,new TypeToken<List<ProductData>>(){}.getType());
                    arrProductData = gson.fromJson(s,new TypeToken<List<ProductData>>(){}.getType());
                    //Log.d(TAG, "Got Products COunt = " + arrProductData.size());
                    if (arrProductData != null && arrProductData.size() > 0) {
                        ImageLoader.getInstance().displayImage(Constants.IMAGE_DOWNLOAD_URL + arrProductData.get(0).getPimg(), imgProductImage, options);

                        txtProductName.setText(arrProductData.get(0).getPtitle());
                        txtProductPrice.setText("Actual Price : $" + arrProductData.get(0).getActual_price() + " | Our Price : " + arrProductData.get(0).getDeal_price(), TextView.BufferType.SPANNABLE);
                        Spannable spannable = (Spannable) txtProductPrice.getText();
                        spannable.setSpan(new StrikethroughSpan(), 15, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        String details = arrProductData.get(0).getPdetails().replace("<br />","\n").trim();
                        txtProductDetails.setText(details);

                    }

                }else{

                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                mGeneralCallBacks.showErrorDialog("Error","Error While Fetching Products Detail");
            }

        }
    }

    class AddToCartTask extends AsyncTask<String,Void,String> {

        HashMap<String,String> data;
        Context context;
        GeneralCallbacks generalCallbacks;
        AddToCartTask(HashMap<String, String> data, Context context,GeneralCallbacks generalCallbacks){
            this.data = data;
            this.context = context;
            this.generalCallbacks = generalCallbacks;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (pDialog == null) {
                pDialog = new ProgressDialog(context);
            }

            pDialog.setMessage(getString(R.string.adding_to_cart));
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if(data == null || data.size() == 0){
                return CommonMethods.callSyncService(this.context, params[0], TAG);
            }else{

                return CommonMethods.callSyncService(this.context,data,  params[0], TAG);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(pDialog != null && pDialog.isShowing()){
                pDialog.dismiss();
            }

            try {
                if (s != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        for(int i = 0; i < jsonArray.length(); i ++){
                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            String count = jsonObject.optString("count");
                            if(!TextUtils.isEmpty(count)){
                                try {
                                    generalCallbacks.showErrorDialog("Success","Items Added successfully");
                                    generalCallbacks.updateCartCount(Integer.parseInt(count));
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    generalCallbacks.showErrorDialog("Error","Error While Adding to Cart");
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                generalCallbacks.showErrorDialog("Error", "Error While Adding to Cart");
            }

        }
    }


    // not called
    private void updateCartCount(int count){
        if (txtItemCount != null) {
            txtItemCount.setText(String.valueOf(count));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mGeneralCallBacks = (GeneralCallbacks) activity;
    }
}
