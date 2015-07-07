package com.app.e10d.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.e10d.Data.ProductData;
import com.app.e10d.HomeScreenActivity;
import com.app.e10d.Interfaces.GeneralCallbacks;
import com.app.e10d.R;
import com.app.e10d.adapter.GridViewWithHeader;
import com.app.e10d.application.E10DApplication;
import com.app.e10d.constants.CommonMethods;
import com.app.e10d.constants.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.co.recruit_mp.android.widget.HeaderFooterGridView;

/**
 * Created by Nilesh on 08/07/15.
 */
public class ListProductsFragment extends Fragment {

    private static final String TAG = "ListProductsFragment";

    int activeProdcutType = HomeScreenActivity.LATEST_PRODUCTS;

    HeaderFooterGridView list;

    GridViewWithHeader adapter;

    GeneralCallbacks mGeneralCallBacks;

    ProgressDialog pDialog;
    ArrayList<ProductData> arrProductData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchProducts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_products_fragment, container, false);
        list = (HeaderFooterGridView) view.findViewById(R.id.HeaderFooterGridView);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header_view_grid, null, false);
        list.addHeaderView(header);
        /*adapter = new GridViewWithHeader(getActivity(),arrProductData);
        list.setAdapter(adapter);*/
        Toast.makeText(getActivity(),"List Fragment",Toast.LENGTH_SHORT).show();
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

            pDialog.setMessage(getString(R.string.fetching_products));
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if(data == null || data.size() == 0){
                return CommonMethods.callSyncService(this.context,  params[0], TAG);
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
                    arrProductData = gson.fromJson(s,new TypeToken<List<ProductData>>(){}.getType());
                    Log.d(TAG,"Got Products COunt = "+arrProductData.size());
                    adapter = new GridViewWithHeader(getActivity(),arrProductData);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else{

                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                mGeneralCallBacks.showErrorDialog("Error","Error While Fetching Products");
            }

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mGeneralCallBacks = (GeneralCallbacks) activity;
    }

    private void fetchProducts(){
        Bundle bundle = getArguments();
        if (bundle != null) {
            activeProdcutType = bundle.getInt("tag");
        }


        if(activeProdcutType == HomeScreenActivity.LATEST_PRODUCTS){
            new GetProductsTask(new HashMap<String,String>(),getActivity()).execute(Constants.LATEST_PRODUCTS_URL);
        }else{
            HashMap<String,String> data = new HashMap<String,String>();
            data.put("cat",String.valueOf(getCategory(activeProdcutType)));
            new GetProductsTask(data,getActivity()).execute(Constants.PRODUCTS_URL);
        }
    }

   /* public int getActiveProdcutType() {
        return activeProdcutType;
    }

    public void setActiveProdcutType(int activeProdcutType) {
        this.activeProdcutType = activeProdcutType;
    }*/

    private int getCategory(int tag){
        switch (tag) {
            case 100:
                return -1;
            //break;
            case 101:
            {
                return  1;

            }
            //break;
            case 102:
            {
                return 2;
            }
            //break;
            case 103:
            {
                return 3;
            }
            //break;

            case 104:
            {
                return 4;
            }
            //break;

            default:
                return -1;
                //break;
        }
    }
}
