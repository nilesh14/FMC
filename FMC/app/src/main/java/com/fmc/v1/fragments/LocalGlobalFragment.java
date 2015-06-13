package com.fmc.v1.fragments;



import com.facebook.AccessToken;
import com.fmc.v1.MainActivity;
import com.fmc.v1.R;
import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.callbacks.SwitchFragmentsCallback;
import com.fmc.v1.constants.CommonMethods;
import com.fmc.v1.constants.Constants;
import com.fmc.v1.view.CircularImageView;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class LocalGlobalFragment extends Fragment implements OnClickListener{
	protected static final String TAG = "WallFragment";
	CircularImageView circularImageView;
	TextView txtWelComeMessage;
	FMCApplication app;
	SharedPreferences mPrefs;
	Switch switchLocalGlobal;
	ImageButton imgbtnOptions;
	Button btnShowWallOrGlobal;
    SwitchFragmentsCallback switchFragmentsCallback;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.wall_local_global_select_layout, container, false);
		
		app = (FMCApplication) getActivity().getApplication();
		mPrefs = app.getmPreffs();
		btnShowWallOrGlobal = (Button) view.findViewById(R.id.btnShowWallOrGlobal);
        btnShowWallOrGlobal.setOnClickListener(this);
        displayLocalOrGlobalButton();
		circularImageView = (CircularImageView) view.findViewById(R.id.circularImageView);
		imgbtnOptions = (ImageButton) view.findViewById(R.id.imgbtnOptions);
		imgbtnOptions.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopupMenu popup = new PopupMenu(getActivity(), v);
				popup.getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());
				
				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						 Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
						
						return true;
					}
				});
				
				popup.show();
			}
		});
		switchLocalGlobal = (Switch) view.findViewById(R.id.switchLocalGlobal);
		//switchLocalGlobal.setSelected(mPrefs.getBoolean(Constants.PREFS_GLOBAL_SELECTED, false));
        switchLocalGlobal.setChecked(mPrefs.getBoolean(Constants.PREFS_GLOBAL_SELECTED, false));
		
		switchLocalGlobal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean isChecked = ((Switch) v).isChecked();
                if (isChecked) {
                    Log.d(TAG, "Global Selected Click");
                    mPrefs.edit().putBoolean(Constants.PREFS_GLOBAL_SELECTED, isChecked).apply();
                    displayLocalOrGlobalButton();
                } else {
                    Log.d(TAG, "Local Selected Click");
                    mPrefs.edit().putBoolean(Constants.PREFS_GLOBAL_SELECTED, isChecked).apply();
                    displayLocalOrGlobalButton();
                }
            }
        });
		
		/*switchLocalGlobal.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					Log.d(TAG, "Global Selected");
				}else{
					Log.d(TAG, "Local Selected");
				}
			}
		});*/
		
		txtWelComeMessage = (TextView) view.findViewById(R.id.txtWelComeMessage);
		txtWelComeMessage.setTypeface(app.getDaydreamer());
		txtWelComeMessage.setText("Hi! "+mPrefs.getString(Constants.PREFS_USER_NAME, ""));
		return view;
	}

    private void displayLocalOrGlobalButton(){
        boolean is_global_selected = mPrefs.getBoolean(Constants.PREFS_GLOBAL_SELECTED,false);
        //btnShowWallOrGlobal.setText(is_global_selected?getString(R.string.show_global):getString(R.string.show_local));
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		new GetProfilePic().execute(AccessToken.getCurrentAccessToken().getUserId());
	}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        switchFragmentsCallback = (SwitchFragmentsCallback) activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnShowWallOrGlobal:
               // if( ((Button)v).getText().toString().equalsIgnoreCase(getString(R.string.show_global))) {
                    WallFragment wallFragment = new WallFragment();
                    switchFragmentsCallback.switchFragment(wallFragment, MainActivity.FRAG_WALL,null);
                //}
                break;
        }
    }

	

    class GetProfilePic extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			return CommonMethods.getFacebookProfilePicture(getActivity(),params[0]);
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

}
