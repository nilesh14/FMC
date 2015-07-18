package com.fmc.v1.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fmc.v1.R;
import com.fmc.v1.adapter.WallAdapter;
import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.callbacks.AsyncCallBacks;
import com.fmc.v1.callbacks.PostNewCommentDialogCallback;
import com.fmc.v1.callbacks.PostNewWallPostDialogCallback;
import com.fmc.v1.callbacks.WallFragCommands;
import com.fmc.v1.constants.CommonMethods;
import com.fmc.v1.constants.Constants;
import com.fmc.v1.data.WallData;
import com.fmc.v1.dialog.PostCommentDialog;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WallFragment extends Fragment implements PostNewCommentDialogCallback,WallFragCommands{
	
	public static final String TAG = "WallFragment";
	private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    WallAdapter adapter;
    ArrayList<WallData> arrWallData = new ArrayList<WallData>();
    LinearLayout linSortHeader,linWritePostContainer;
    ProgressDialog pDialog;
    HashMap<String, String> mapData;
    PostNewWallPostDialogCallback postNewWallPostDialogCallback;
    SharedPreferences mPrefs = FMCApplication.mPreffs;
    PostCommentDialog postCommentDialog;
    Animation slideFromTop,slideFromBottom;
    TextView txtSortBy,txtWritePost,txtSharePhoto;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.wall, container, false);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        linSortHeader = (LinearLayout) view.findViewById(R.id.linSortHeader);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mapData = new HashMap<String, String>();
        txtSortBy = (TextView) view.findViewById(R.id.txtSortBy);
        slideFromTop = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_from_top);
        slideFromBottom = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_from_bottom);
        linWritePostContainer = (LinearLayout) view.findViewById(R.id.linWritePostContainer);
        txtWritePost = (TextView) view.findViewById(R.id.txtWritePost);
        txtWritePost.setTypeface(FMCApplication.ubuntu);
        txtSharePhoto = (TextView) view.findViewById(R.id.txtSharePhoto);
        txtSharePhoto.setTypeface(FMCApplication.ubuntu);

        linWritePostContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postNewWallPostDialogCallback.showAddNewWallPostDialog();
            }
        });

        //Toast.makeText(getActivity(),"Global Selected : "+mPrefs.getBoolean(Constants.PREFS_GLOBAL_SELECTED,false),Toast.LENGTH_SHORT).show();
        
        adapter = new WallAdapter(arrWallData,this);
        mRecyclerView.setAdapter(adapter);

        new Handler().postDelayed(getWallData, 200);
        
		return view;
	}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        postNewWallPostDialogCallback = (PostNewWallPostDialogCallback) activity;
    }

    Runnable getWallData = new Runnable() {
        @Override
        public void run() {
            mapData.clear();
            mapData.put(Constants.KEY_CITY, "");
            new GetWallData(mapData).execute(Constants.GET_ALL_WALL_DATA_URL);
        }
    };



    @Override
    public void postNewComment(WallData wallData) {
        HashMap<String, String> data = new HashMap<>();
        data.put("uid",String.valueOf(mPrefs.getInt(Constants.PREFS_UID, 0)));
        data.put("post_id",String.valueOf(wallData.getPostID()));
        data.put("comment",wallData.getCommentText());
        new PostComment(data,wallData).execute(Constants.COMMENT_URL);
    }

    @Override
    public void showFilterOptions(boolean showView) {


        if(linSortHeader.getVisibility() == View.GONE){

            linSortHeader.setVisibility(View.VISIBLE);
            txtSortBy.setVisibility(View.VISIBLE);
            linSortHeader.startAnimation(slideFromTop);
            //txtSortBy.startAnimation(slideFromTop);

            /*ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(linSortHeader,"translationY",-100f,0f);
            objectAnimator.setDuration(350);
            objectAnimator.start();*/
        }else{
            slideFromBottom.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    linSortHeader.setVisibility(View.GONE);
                    txtSortBy.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            linSortHeader.startAnimation(slideFromBottom);
            //txtSortBy.startAnimation(slideFromBottom);

            /*ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(linSortHeader,"translationY",0f,-100f);
            objectAnimator.setDuration(350);
            objectAnimator.start();*/
        }
    }

    class GetWallData extends AsyncTask<String, Void, String>{

    	HashMap<String, String> data;
    	public GetWallData(HashMap<String, String> data) {
			// TODO Auto-generated constructor stub
    		this.data = data;
		}
    	
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		
    		if(pDialog == null){
				pDialog = new ProgressDialog(getActivity());	
				pDialog.dismiss();
			}
			pDialog.setCancelable(false);
			pDialog.setMessage(getResources().getString(R.string.fetching_WallData));
			pDialog.show();
    	}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			return CommonMethods.callSyncService(getActivity(), data, params[0], TAG);
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
					parseWallData(jArr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}else{
                Toast.makeText(getActivity(),getString(R.string.no_internet_or_error),Toast.LENGTH_SHORT).show();
            }
		}
    	
    }

	private void parseWallData(JSONArray jArr){
		arrWallData.clear();
		for(int i = 0;i < jArr.length(); i ++){
			JSONObject jObj = jArr.optJSONObject(i);

            WallData wallData = new WallData();
            wallData.setName(jObj.optString(Constants.KEY_NAME));
            wallData.setEmail(jObj.optString(Constants.KEY_EMAIL));
            wallData.setLikeCount(jObj.optInt(Constants.KEY_LIKES));
            wallData.setPostID(jObj.optInt(Constants.KEY_POST_ID));
            wallData.setTextPost(jObj.optString(Constants.KEY_POST));
            wallData.setTime_elapsed(jObj.optString(Constants.KEY_TIME_ELAPSED));
            arrWallData.add(wallData);
            //adapter.addWallItem(wallData);
		}
        adapter.notifyDataSetChanged();
    }

    public void postLike(WallData wallData){
        HashMap<String, String> data = new HashMap<>();
        data.put("uid",String.valueOf(mPrefs.getInt(Constants.PREFS_UID,0)));
        data.put("post_id",String.valueOf(wallData.getPostID()));
        new PostLike(data,wallData).execute(Constants.LIKE_POST_URL);
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
                pDialog = new ProgressDialog(getActivity());
                pDialog.dismiss();
            }
            pDialog.setCancelable(false);
            pDialog.setMessage(getResources().getString(R.string.please_wait));
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            return CommonMethods.callSyncService(getActivity(), data, params[0], TAG);
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
                    parseLikeResponse(jArr,wallData);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }else{
                Toast.makeText(getActivity(),getString(R.string.no_internet_or_error),Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void parseLikeResponse(JSONArray jsonArray, WallData wallData){
        for(int i = 0; i < jsonArray.length(); i ++){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if(jsonObject != null){
                String code = jsonObject.optString("code");
                if(code.equalsIgnoreCase("1")){
                    int position = adapter.removeWallItem(wallData);
                    int count = wallData.getLikeCount() + 1;
                    wallData.setLikeCount(count);
                    adapter.addWallItematPosition(wallData,position);
                    break;
                }
            }
        }
    }

    public void postComment(WallData wallData){

        if (postCommentDialog == null) {
            postCommentDialog = new PostCommentDialog(getActivity(),R.style.CommentDialogtheme,this,wallData);
        }

        postCommentDialog.setOwnerActivity(getActivity());
        postCommentDialog.show();

    }

    class PostComment extends AsyncTask<String, Void, String>{

        HashMap<String, String> data;
        WallData wallData;
        public PostComment(HashMap<String, String> data , WallData wallData) {
            // TODO Auto-generated constructor stub
            this.data = data;
            this.wallData = wallData;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            if(pDialog == null){
                pDialog = new ProgressDialog(getActivity());
                pDialog.dismiss();
            }
            pDialog.setCancelable(false);
            pDialog.setMessage(getResources().getString(R.string.please_wait));
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            return CommonMethods.callSyncService(getActivity(), data, params[0], TAG);
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
                    parseCommentResponse(jArr, wallData);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }else{
                Toast.makeText(getActivity(),getString(R.string.no_internet_or_error),Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void parseCommentResponse(JSONArray jsonArray, WallData wallData){
        for(int i = 0; i < jsonArray.length(); i ++){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if(jsonObject != null){
                String code = jsonObject.optString("code");
                if(code.equalsIgnoreCase("1")){
                    refreshData();
                    /*int position = adapter.removeWallItem(wallData);
                    int count = wallData.getLikeCount() + 1;
                    wallData.setLikeCount(count);
                    adapter.addWallItematPosition(wallData,position);*/
                    break;
                }
            }
        }
    }

    public void refreshData(){
        new Handler().postDelayed(getWallData, 200);
    }


}
