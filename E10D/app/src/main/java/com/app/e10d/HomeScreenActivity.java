package com.app.e10d;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.e10d.Fragments.ListProductsFragment;
import com.app.e10d.Interfaces.GeneralCallbacks;
import com.app.e10d.Interfaces.HeaderItemCallbacks;
import com.app.e10d.adapter.GridViewWithHeader;
import com.app.e10d.application.E10DApplication;
import com.app.e10d.constants.CommonMethods;
import com.app.e10d.constants.Constants;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.HashMap;


/**
 * Created by Nilesh on 07/07/15.
 */
public class HomeScreenActivity extends FragmentActivity implements GeneralCallbacks, HeaderItemCallbacks, View.OnClickListener {

    private static final String TAG = "HomeScreenActivity";
    public static int FRAG_GRID_VIEW = 1;
    public static int FRAG_GRID_VIEW_PRODUCT_DETAIL = 2;

    public static int LATEST_PRODUCTS = 100;
    public static int APPARELS = 101;
    public static int ELECTRONICS = 102;
    public static int HEALTH = 103;
    public static int JEWELRY = 104;

    DrawerLayout drawer;

    ImageView imgMenu;

    public static int USER = 201;
    public static int HOME = 202;
    public static int SEARCH = 203;
    public static int SHOPPING = 204;
    public static int INVITE = 205;
    public static int ORDER = 206;
    public static int SETTING = 207;

    public int activeSideBarCell = HOME;
    TextView txtCartCount;

    LinearLayout linLatestContainer, linApparelsContainer, linElectronicsContainer, linHealthContainer, linJewelryContainer, drawerRight;

    RelativeLayout relUserContainer, relHomeContainer, relSearchContainer, relShoppingContainer, relInviteContainer, relOrderContainer, relSettingContainer;
    int currentFrag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_activity);

        relHomeContainer = (RelativeLayout) findViewById(R.id.relHomeContainer);
        relHomeContainer.setOnClickListener(this);
        relUserContainer = (RelativeLayout) findViewById(R.id.relUserContainer);
        relSearchContainer = (RelativeLayout) findViewById(R.id.relSearchContainer);
        relSearchContainer.setOnClickListener(this);
        relShoppingContainer = (RelativeLayout) findViewById(R.id.relShoppingContainer);
        relShoppingContainer.setOnClickListener(this);
        relInviteContainer = (RelativeLayout) findViewById(R.id.relInviteContainer);
        relInviteContainer.setOnClickListener(this);
        relOrderContainer = (RelativeLayout) findViewById(R.id.relOrderContainer);
        relOrderContainer.setOnClickListener(this);
        relSettingContainer = (RelativeLayout) findViewById(R.id.relSettingContainer);
        relSettingContainer.setOnClickListener(this);

        linLatestContainer = (LinearLayout) findViewById(R.id.linLatestContainer);
        linApparelsContainer = (LinearLayout) findViewById(R.id.linApparelsContainer);
        linElectronicsContainer = (LinearLayout) findViewById(R.id.linElectronicsContainer);
        linHealthContainer = (LinearLayout) findViewById(R.id.linHealthContainer);
        linJewelryContainer = (LinearLayout) findViewById(R.id.linJewelryContainer);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawerRight = (LinearLayout) findViewById(R.id.drawerRight);
        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        txtCartCount = (TextView) findViewById(R.id.txtCartCount);

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawer.isDrawerOpen(drawerRight)) {
                    drawer.closeDrawer(drawerRight);
                } else {
                    drawer.openDrawer(drawerRight);
                }
            }
        });

        //drawer.openDrawer(drawerRight);

        linJewelryContainer.setOnClickListener(this);
        linLatestContainer.setOnClickListener(this);
        linApparelsContainer.setOnClickListener(this);
        linElectronicsContainer.setOnClickListener(this);
        linHealthContainer.setOnClickListener(this);

        switchFragment(FRAG_GRID_VIEW, new ListProductsFragment());

        getCartCountAndUpdate();
    }

    private void updateSideBarSelectionBGColor() {

        if (activeSideBarCell == HOME) {
            relHomeContainer.setBackgroundColor(getResources().getColor(R.color.header_red));
            relUserContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relSearchContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relShoppingContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relInviteContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relOrderContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relSettingContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));

        }else if(activeSideBarCell == SEARCH){
            relHomeContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relUserContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relSearchContainer.setBackgroundColor(getResources().getColor(R.color.header_red));
            relShoppingContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relInviteContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relOrderContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relSettingContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
        }else if(activeSideBarCell == SHOPPING){
            relHomeContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relUserContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relSearchContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relShoppingContainer.setBackgroundColor(getResources().getColor(R.color.header_red));
            relInviteContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relOrderContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relSettingContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
        }else if(activeSideBarCell == INVITE){
            relHomeContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relUserContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relSearchContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relShoppingContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relInviteContainer.setBackgroundColor(getResources().getColor(R.color.header_red));
            relOrderContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relSettingContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
        }else if(activeSideBarCell == ORDER){
            relHomeContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relUserContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relSearchContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relShoppingContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relInviteContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relOrderContainer.setBackgroundColor(getResources().getColor(R.color.header_red));
            relSettingContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
        }else if(activeSideBarCell == SETTING){
            relHomeContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relUserContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relSearchContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relShoppingContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relInviteContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relOrderContainer.setBackgroundColor(getResources().getColor(R.color.side_bg_color));
            relSettingContainer.setBackgroundColor(getResources().getColor(R.color.header_red));
        }

    }

    @Override
    public void showErrorDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    @Override
    public void switchFragment(int fragID, android.support.v4.app.Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragID == FRAG_GRID_VIEW) {
            getSupportFragmentManager().popBackStack();
            ft.replace(R.id.frame, fragment)/*.addToBackStack("FRAG_GRID_VIEW")*/.commit();
        }

        if (fragID == FRAG_GRID_VIEW_PRODUCT_DETAIL) {
            //getSupportFragmentManager().popBackStack();
            ft.replace(R.id.frame, fragment).addToBackStack("FRAG_GRID_DETAIL").commit();
        }
    }

    @Override
    public void startNewActivity(Intent intent) {

    }

    @Override
    public void updateCartCount(int count) {
        if(txtCartCount != null){
            txtCartCount.setText(String.valueOf(count));
        }
    }

    @Override
    public int getCartCountAndUpdate() {
        HashMap<String,String> data = new HashMap<String,String>();
        data.put("uid", E10DApplication.mPrefs.getString(Constants.PREFS_ID,""));
        new GetCartCountTask(data,HomeScreenActivity.this,HomeScreenActivity.this).execute(Constants.GET_CART_COUNT_URL);
        return 0;
    }

    @Override
    public void onLatestClick() {

        switchFragment(FRAG_GRID_VIEW, prepareProductFragment(LATEST_PRODUCTS));
    }

    @Override
    public void onApperalsClick() {

        switchFragment(FRAG_GRID_VIEW, prepareProductFragment(APPARELS));
    }

    @Override
    public void onElectronicsClick() {

        switchFragment(FRAG_GRID_VIEW, prepareProductFragment(ELECTRONICS));
    }

    @Override
    public void onHealthClick() {

        switchFragment(FRAG_GRID_VIEW, prepareProductFragment(HEALTH));
    }

    @Override
    public void onJewelryClick() {
        switchFragment(FRAG_GRID_VIEW, prepareProductFragment(JEWELRY));
    }

    private android.support.v4.app.Fragment prepareProductFragment(int TYPE) {
        ListProductsFragment fragment = new ListProductsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tag", TYPE);
        fragment.setArguments(bundle);
        //fragment.setActiveProdcutType(TYPE);
        return fragment;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.linLatestContainer:
                onLatestClick();
                break;
            case R.id.linApparelsContainer:
                onApperalsClick();
                break;
            case R.id.linElectronicsContainer:
                onElectronicsClick();
                break;
            case R.id.linHealthContainer:
                onHealthClick();
                break;
            case R.id.linJewelryContainer:
                onJewelryClick();
                break;

            case R.id.relHomeContainer:
                activeSideBarCell = HOME;
                handleSideBarClick();
                break;
            case R.id.relSearchContainer:
                activeSideBarCell = SEARCH;
                handleSideBarClick();
                break;
            case R.id.relShoppingContainer:
                activeSideBarCell = SHOPPING;
                handleSideBarClick();
                break;
            case R.id.relInviteContainer:
                activeSideBarCell = INVITE;
                handleSideBarClick();
                break;
            case R.id.relOrderContainer:
                activeSideBarCell = ORDER;
                handleSideBarClick();
                break;
            case R.id.relSettingContainer:
                activeSideBarCell = SETTING;
                handleSideBarClick();
                break;

        }

    }

    class GetCartCountTask extends AsyncTask<String,Void,String> {

        HashMap<String,String> data;
        Context context;
        GeneralCallbacks generalCallbacks;
        GetCartCountTask(HashMap<String, String> data, Context context,GeneralCallbacks generalCallbacks){
            this.data = data;
            this.context = context;
            this.generalCallbacks = generalCallbacks;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

            try {
                if (s != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        for(int i = 0; i < jsonArray.length(); i ++){
                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            String count = jsonObject.optString("count");
                            if(!TextUtils.isEmpty(count)){
                                try {
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

                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                //generalCallbacks.showErrorDialog("Error","Error While Adding to Cart");
            }

        }
    }

    private void handleSideBarClick() {
        updateSideBarSelectionBGColor();
        drawer.closeDrawer(drawerRight);

        if (activeSideBarCell == ORDER) {

        }

        if (activeSideBarCell == SEARCH) {

        }

        if (activeSideBarCell == SHOPPING) {

        }

        if (activeSideBarCell == HOME) {

        }

    }
}
