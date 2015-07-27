package com.fmc.v1;


import com.facebook.AccessToken;
import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.callbacks.PostNewWallPostDialogCallback;
import com.fmc.v1.callbacks.SwitchFragmentsCallback;
import com.fmc.v1.callbacks.WallFragCommands;
import com.fmc.v1.constants.CommonMethods;
import com.fmc.v1.constants.Constants;
import com.fmc.v1.data.WallData;
import com.fmc.v1.dialog.AddWallPostDialog;
import com.fmc.v1.dialog.SharePhoto;
import com.fmc.v1.fragments.LocalGlobalFragment;
import com.fmc.v1.fragments.ProfileFragment;
import com.fmc.v1.fragments.WallFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends Activity implements SwitchFragmentsCallback, PostNewWallPostDialogCallback, View.OnClickListener {

    private static final String TAG = "MainActivity";
    public static final int FRAG_WALL = 1;
    public static final int FRAG_LOCAL_OR_GLOBAL = 2;
    public static final int FRAG_FAQS = 3;
    public static final int FRAG_BITCH = 4;
    public static final int FRAG_PROFILE = 5;
    public static final int FRAG_MORE = 6;
    private static final int RESULT_LOAD_IMG = 1000;

    private static int activeFragment = 0;
    //Button btnWall, btnFAQS, btnBitch, btnProfile, btnMore;
    LinearLayout linWall,linBitch,linFAQ,linProfile,linMore;
    TextView txtMore,txtProfile,txtFAQ,txtBitch,txtWall;
    //CircularImageView circularImageView;
    //RelativeLayout relTopContainerContainer;
    ProgressDialog pDialog;
    Fragment currentFragmentInstance;
    ImageView imgAddPost,imgFilter;
    WallFragCommands wallFragCommands;
    //Switch switchLocalGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Log.d(TAG, "User ID " + AccessToken.getCurrentAccessToken().getUserId());
        //circularImageView = (CircularImageView) findViewById(R.id.circularImageView);
        linBitch = (LinearLayout) findViewById(R.id.linBitch);
        linFAQ = (LinearLayout) findViewById(R.id.linFAQ);
        linMore = (LinearLayout) findViewById(R.id.linMore);
        linProfile = (LinearLayout) findViewById(R.id.linProfile);
        linProfile.setOnClickListener(this);
        linWall = (LinearLayout) findViewById(R.id.linWall);
        linWall.setOnClickListener(this);
        txtBitch = (TextView) findViewById(R.id.txtBitch);
        txtFAQ = (TextView) findViewById(R.id.txtFAQ);
        txtMore = (TextView) findViewById(R.id.txtMore);
        txtProfile = (TextView) findViewById(R.id.txtProfile);
        txtWall = (TextView) findViewById(R.id.txtWall);

        txtWall.setTypeface(FMCApplication.ubuntu);
        txtProfile.setTypeface(FMCApplication.ubuntu);
        txtMore.setTypeface(FMCApplication.ubuntu);
        txtFAQ.setTypeface(FMCApplication.ubuntu);
        txtBitch.setTypeface(FMCApplication.ubuntu);

        //relTopContainerContainer = (RelativeLayout) findViewById(R.id.relTopContainerContainer);
        imgAddPost = (ImageView) findViewById(R.id.imgAddPost);
        imgFilter = (ImageView) findViewById(R.id.imgFilter);
        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wallFragCommands != null) {
                    wallFragCommands.showFilterOptions(true);
                    if (currentFragmentInstance instanceof WallFragment) {
                        boolean isFilterBarVisible = ((WallFragment) currentFragmentInstance).isFilterBarOpen();
                        if(isFilterBarVisible){
                            imgFilter.setImageResource(R.drawable.filter_selected);
                        }else{
                            imgFilter.setImageResource(R.drawable.filter);
                        }
                    }
                }
            }
        });
        //switchLocalGlobal = (Switch) findViewById(R.id.switchLocalGlobal);
        //switchLocalGlobal.setSelected(mPrefs.getBoolean(Constants.PREFS_GLOBAL_SELECTED, false));
        //switchLocalGlobal.setChecked(FMCApplication.mPreffs.getBoolean(Constants.PREFS_GLOBAL_SELECTED, false));

        /*switchLocalGlobal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean isChecked = ((Switch) v).isChecked();
                if (isChecked) {
                    Log.d(TAG, "Global Selected Click");
                    FMCApplication.mPreffs.edit().putBoolean(Constants.PREFS_GLOBAL_SELECTED, isChecked).apply();
                    if (currentFragmentInstance instanceof WallFragment) {
                        ((WallFragment) currentFragmentInstance).refreshData();
                    }
                } else {
                    Log.d(TAG, "Local Selected Click");
                    FMCApplication.mPreffs.edit().putBoolean(Constants.PREFS_GLOBAL_SELECTED, isChecked).apply();
                    if (currentFragmentInstance instanceof WallFragment) {
                        ((WallFragment) currentFragmentInstance).refreshData();
                    }
                }
            }
        });*/

        /*imgAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPostDialog();
            }
        });*/

        /*if (FMCApplication.loggedinUserPic != null) {
            circularImageView.setImageBitmap(FMCApplication.loggedinUserPic);
        }else{

            new GetProfilePic().execute(AccessToken.getCurrentAccessToken().getUserId());
        }*/

        boolean showProfileFragment = getIntent().getBooleanExtra("show_profile",false);
        if(showProfileFragment){
            switchNewFragment(FRAG_PROFILE);
        }else{
            switchNewFragment(FRAG_WALL);
        }

    }

    private void showAddPostDialog() {
        AddWallPostDialog addDialog = new AddWallPostDialog(MainActivity.this, R.style.CommentDialogtheme, this);
        addDialog.setOwnerActivity(MainActivity.this);
        addDialog.show();
    }

    @Override
    public void switchFragment(Fragment fragment, int fragID, Object extras) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        activeFragment = fragID;

       /* if (fragID == FRAG_WALL) {
            //switchLocalGlobal.setChecked(FMCApplication.mPreffs.getBoolean(Constants.PREFS_GLOBAL_SELECTED, false));
            relTopContainerContainer.setVisibility(View.VISIBLE);
        } else if (fragID == FRAG_LOCAL_OR_GLOBAL) {

            relTopContainerContainer.setVisibility(View.GONE);
        }*/

        //updateButtonBG();
        currentFragmentInstance = fragment;
        transaction.replace(R.id.frame, fragment).commit();
        updateButtonBG();
    }

    @Override
    public void postNewWallMessage(WallData wallData) {
        if (wallData != null) {
            HashMap<String, String> data = new HashMap<>();
            data.put("uid", String.valueOf(FMCApplication.mPreffs.getInt(Constants.PREFS_UID, 0)));
            data.put("post", wallData.getTextPost());
            new PostWallMessage(data, wallData).execute(Constants.ADD_POST__URL);
        }
    }

    @Override
    public void showAddNewWallPostDialog() {
        showAddPostDialog();
    }

    @Override
    public void sharePhoto() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linWall:
                switchNewFragment(FRAG_WALL);
                break;
            case R.id.linProfile:
                switchNewFragment(FRAG_PROFILE);
                break;
        }
    }

    class PostWallMessage extends AsyncTask<String, Void, String> {

        HashMap<String, String> data;
        WallData wallData;

        public PostWallMessage(HashMap<String, String> data, WallData wallData) {
            // TODO Auto-generated constructor stub
            this.data = data;
            this.wallData = wallData;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            if (pDialog == null) {
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.dismiss();
            }
            pDialog.setCancelable(false);
            pDialog.setMessage(getResources().getString(R.string.please_wait));
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            return CommonMethods.callSyncService(MainActivity.this, data, params[0], TAG);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (result != null) {
                try {
                    JSONArray jArr = new JSONArray(result);
                    parsePostWallMessageResponse(jArr, wallData);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                SharePhoto dialog = new SharePhoto(MainActivity.this,R.style.CommentDialogtheme);
                dialog.setImage(BitmapFactory
                        .decodeFile(imgDecodableString));
                dialog.setOwnerActivity(MainActivity.this);
                dialog.show();
                /*ImageView imgView = (ImageView) findViewById(R.id.imgView);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));*/

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }
    }

    private void parsePostWallMessageResponse(JSONArray jsonArray, WallData wallData) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject != null) {
                String code = jsonObject.optString("code");
                if (code.equalsIgnoreCase("1")) {
                    //refresh Wall fragment here
                    if (currentFragmentInstance instanceof WallFragment) {
                        ((WallFragment) currentFragmentInstance).refreshData();
                    }
                    /*int position = adapter.removeWallItem(wallData);
                    int count = wallData.getLikeCount() + 1;
                    wallData.setLikeCount(count);
                    adapter.addWallItematPosition(wallData,position);*/
                    break;
                }
            }
        }
    }

    class GetProfilePic extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub

            return CommonMethods.getSmallFacebookProfilePicture(MainActivity.this, params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                //circularImageView.setImageBitmap(result);
                FMCApplication.loggedinUserPic = result;
            }
        }

    }

    private void switchNewFragment(int frag) {
        /*FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //transaction.replace(R.id.frame, new LocalGlobalFragment()).commit();
		activeFragment = frag;*/

        if (frag == FRAG_WALL) {
            WallFragment fragment = new WallFragment();
            this.wallFragCommands = fragment;
            currentFragmentInstance = fragment;
            switchFragment(fragment, FRAG_WALL, null);
			/*transaction.replace(R.id.frame, new WallFragment()).commit();
            relTopContainerContainer.setVisibility(View.VISIBLE);*/
        } else if (frag == FRAG_LOCAL_OR_GLOBAL) {
            switchFragment(new LocalGlobalFragment(), FRAG_LOCAL_OR_GLOBAL, null);
            /*transaction.replace(R.id.frame, new LocalGlobalFragment()).commit();
            relTopContainerContainer.setVisibility(View.GONE);*/
        } else if (frag == FRAG_PROFILE) {
            ProfileFragment fragment = new ProfileFragment();
            currentFragmentInstance = fragment;
            switchFragment(fragment, FRAG_PROFILE, null);
        }

    }

    private void updateButtonBG() {
        switch (activeFragment) {
            case FRAG_WALL:
                linWall.setBackgroundColor(getResources().getColor(R.color.tab_selected_color));
                linBitch.setBackgroundColor(getResources().getColor(R.color.main_color_red));
                linFAQ.setBackgroundColor(getResources().getColor(R.color.main_color_red));
                linProfile.setBackgroundColor(getResources().getColor(R.color.main_color_red));
                linMore.setBackgroundColor(getResources().getColor(R.color.main_color_red));

                break;
            case FRAG_PROFILE:
                linWall.setBackgroundColor(getResources().getColor(R.color.main_color_red));
                linBitch.setBackgroundColor(getResources().getColor(R.color.main_color_red));
                linFAQ.setBackgroundColor(getResources().getColor(R.color.main_color_red));
                linProfile.setBackgroundColor(getResources().getColor(R.color.tab_selected_color));
                linMore.setBackgroundColor(getResources().getColor(R.color.main_color_red));

                break;
        }
    }

}
