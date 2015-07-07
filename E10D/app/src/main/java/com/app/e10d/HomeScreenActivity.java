package com.app.e10d;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.app.e10d.Fragments.ListProductsFragment;
import com.app.e10d.Interfaces.GeneralCallbacks;
import com.app.e10d.Interfaces.HeaderItemCallbacks;
import com.app.e10d.adapter.GridViewWithHeader;


/**
 * Created by Nilesh on 07/07/15.
 */
public class HomeScreenActivity extends FragmentActivity implements GeneralCallbacks,HeaderItemCallbacks, View.OnClickListener{

    public static int FRAG_GRID_VIEW = 1;

    public static int LATEST_PRODUCTS = 100;
    public static int APPARELS = 101;
    public static int ELECTRONICS = 103;
    public static int HEALTH = 104;
    public static int JEWELRY = 105;

    LinearLayout linLatestContainer,linApparelsContainer,linElectronicsContainer,linHealthContainer,linJewelryContainer;

    int currentFrag = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_activity);

        linLatestContainer = (LinearLayout) findViewById(R.id.linLatestContainer);
        linApparelsContainer = (LinearLayout) findViewById(R.id.linApparelsContainer);
        linElectronicsContainer = (LinearLayout) findViewById(R.id.linElectronicsContainer);
        linHealthContainer = (LinearLayout) findViewById(R.id.linHealthContainer);
        linJewelryContainer = (LinearLayout) findViewById(R.id.linJewelryContainer);

        linJewelryContainer.setOnClickListener(this);
        linLatestContainer.setOnClickListener(this);
        linApparelsContainer.setOnClickListener(this);
        linElectronicsContainer.setOnClickListener(this);
        linHealthContainer.setOnClickListener(this);

        switchFragment(FRAG_GRID_VIEW , new ListProductsFragment());
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
        if(fragID == FRAG_GRID_VIEW) {
            getSupportFragmentManager().popBackStack();
            ft.replace(R.id.frame, fragment).commit();
        }
    }

    @Override
    public void startNewActivity(Intent intent) {

    }

    @Override
    public void onLatestClick() {

        switchFragment(FRAG_GRID_VIEW,prepareProductFragment(LATEST_PRODUCTS));
    }

    @Override
    public void onApperalsClick() {

        switchFragment(FRAG_GRID_VIEW,prepareProductFragment(APPARELS));
    }

    @Override
    public void onElectronicsClick() {

        switchFragment(FRAG_GRID_VIEW,prepareProductFragment(ELECTRONICS));
    }

    @Override
    public void onHealthClick() {

        switchFragment(FRAG_GRID_VIEW,prepareProductFragment(HEALTH));
    }

    @Override
    public void onJewelryClick() {
        switchFragment(FRAG_GRID_VIEW,prepareProductFragment(JEWELRY));
    }

    private android.support.v4.app.Fragment prepareProductFragment(int TYPE){
        ListProductsFragment fragment = new ListProductsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tag", TYPE);
        fragment.setArguments(bundle);
        //fragment.setActiveProdcutType(TYPE);
        return fragment;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
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

        }

    }
}
