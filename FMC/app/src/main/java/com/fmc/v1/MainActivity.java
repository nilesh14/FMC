package com.fmc.v1;


import com.facebook.AccessToken;
import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.callbacks.PostNewWallPostDialogCallback;
import com.fmc.v1.callbacks.SwitchFragmentsCallback;
import com.fmc.v1.constants.CommonMethods;
import com.fmc.v1.constants.Constants;
import com.fmc.v1.data.WallData;
import com.fmc.v1.dialog.AddWallPostDialog;
import com.fmc.v1.fragments.LocalGlobalFragment;
import com.fmc.v1.fragments.WallFragment;
import com.fmc.v1.view.CircularImageView;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends Activity implements SwitchFragmentsCallback,PostNewWallPostDialogCallback,View.OnClickListener{

	private static final String TAG = "MainActivity";
	public static final int FRAG_WALL = 1;
    public static final int FRAG_LOCAL_OR_GLOBAL = 2;
    public static final int FRAG_FAQS = 3;
    public static final int FRAG_BITCH = 4;
    public static final int FRAG_MORE = 5;

	private static int activeFragment = 0;
	Button btnWall,btnFAQS,btnBitch,btnMore;
	CircularImageView circularImageView;
	RelativeLayout relTopContainerContainer;
    ProgressDialog pDialog;
    Fragment currentFragmentInstance;
    ImageView imgAddPost;
    Switch switchLocalGlobal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		Log.d(TAG, "User ID " + AccessToken.getCurrentAccessToken().getUserId());
		circularImageView = (CircularImageView) findViewById(R.id.circularImageView);
		btnWall = (Button) findViewById(R.id.btnWall);
        btnWall.setOnClickListener(this);
		btnBitch = (Button) findViewById(R.id.btnBitch);
		btnFAQS = (Button) findViewById(R.id.btnFAQS);
		btnMore = (Button) findViewById(R.id.btnMore);
		relTopContainerContainer = (RelativeLayout) findViewById(R.id.relTopContainerContainer);
        imgAddPost = (ImageView) findViewById(R.id.imgAddPost);
        switchLocalGlobal = (Switch) findViewById(R.id.switchLocalGlobal);
        //switchLocalGlobal.setSelected(mPrefs.getBoolean(Constants.PREFS_GLOBAL_SELECTED, false));
        switchLocalGlobal.setChecked(FMCApplication.mPreffs.getBoolean(Constants.PREFS_GLOBAL_SELECTED, false));

        switchLocalGlobal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean isChecked = ((Switch) v).isChecked();
                if (isChecked) {
                    Log.d(TAG, "Global Selected Click");
                    FMCApplication.mPreffs.edit().putBoolean(Constants.PREFS_GLOBAL_SELECTED, isChecked).apply();
                    if(currentFragmentInstance instanceof WallFragment){
                        ((WallFragment)currentFragmentInstance).refreshData();
                    }
                } else {
                    Log.d(TAG, "Local Selected Click");
                    FMCApplication.mPreffs.edit().putBoolean(Constants.PREFS_GLOBAL_SELECTED, isChecked).apply();
                    ((WallFragment)currentFragmentInstance).refreshData();
                }
            }
        });

        imgAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPostDialog();
            }
        });

        new GetProfilePic().execute(AccessToken.getCurrentAccessToken().getUserId());
		switchNewFragment(FRAG_LOCAL_OR_GLOBAL);
	}

    private void showAddPostDialog(){
        AddWallPostDialog addDialog = new AddWallPostDialog(MainActivity.this,R.style.CommentDialogtheme,this);
        addDialog.setOwnerActivity(MainActivity.this);
        addDialog.show();
    }

	@Override
	public void switchFragment(Fragment fragment, int fragID, Object extras) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        activeFragment = fragID;

        if(fragID == FRAG_WALL){
            switchLocalGlobal.setChecked(FMCApplication.mPreffs.getBoolean(Constants.PREFS_GLOBAL_SELECTED, false));
            relTopContainerContainer.setVisibility(View.VISIBLE);
        }else if(fragID == FRAG_LOCAL_OR_GLOBAL){

            relTopContainerContainer.setVisibility(View.GONE);
        }

        //updateButtonBG();
        currentFragmentInstance = fragment;
        transaction.replace(R.id.frame,fragment).commit();
        updateButtonBG();
	}

    @Override
    public void postNewWallMessage(WallData wallData) {
        if (wallData != null) {
            HashMap<String, String> data = new HashMap<>();
            data.put("uid", String.valueOf(FMCApplication.mPreffs.getInt(Constants.PREFS_UID,0)));
            data.put("post",wallData.getTextPost());
            new PostLike(data,wallData).execute(Constants.ADD_POST__URL);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnWall:
                switchNewFragment(FRAG_WALL);
                break;
        }
    }

    class PostLike extends AsyncTask<String, Void, String>{

        HashMap<String, String> data;
        WallData wallData;
        public PostLike(HashMap<String, String> data , WallData wallData) {
            // TODO Auto-generated constructor stub
            this.data = data;
            this.wallData = wallData;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            if(pDialog == null){
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
                if(pDialog != null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if(result != null){
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

    private void parsePostWallMessageResponse(JSONArray jsonArray, WallData wallData) {
        for(int i = 0; i < jsonArray.length(); i ++){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if(jsonObject != null){
                String code = jsonObject.optString("code");
                if(code.equalsIgnoreCase("1")){
                    //refresh Wall fragment here
                    if(currentFragmentInstance instanceof WallFragment){
                        ((WallFragment)currentFragmentInstance).refreshData();
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

    class GetProfilePic extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			return CommonMethods.getSmallFacebookProfilePicture(MainActivity.this, params[0]);
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result != null){
				circularImageView.setImageBitmap(result);
			}
		}
		
	}
	
	private void switchNewFragment(int frag){
		/*FragmentTransaction transaction = getFragmentManager().beginTransaction();
		//transaction.replace(R.id.frame, new LocalGlobalFragment()).commit();
		activeFragment = frag;*/
		
		if(frag == FRAG_WALL){
            switchFragment(new WallFragment(), FRAG_WALL, null);
			/*transaction.replace(R.id.frame, new WallFragment()).commit();
            relTopContainerContainer.setVisibility(View.VISIBLE);*/
		}else if(frag == FRAG_LOCAL_OR_GLOBAL){
            switchFragment(new LocalGlobalFragment(),FRAG_LOCAL_OR_GLOBAL,null);
            /*transaction.replace(R.id.frame, new LocalGlobalFragment()).commit();
            relTopContainerContainer.setVisibility(View.GONE);*/
        }
		
	}
	
	private void updateButtonBG(){
		if(activeFragment == FRAG_WALL){
			btnWall.setBackgroundColor(getResources().getColor(R.color.selected_yellow));
			btnBitch.setBackgroundColor(getResources().getColor(R.color.main_color_red));
			btnFAQS.setBackgroundColor(getResources().getColor(R.color.main_color_red));
			btnMore.setBackgroundColor(getResources().getColor(R.color.main_color_red));
		}
	}
}