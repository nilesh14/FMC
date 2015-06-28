package com.fmc.v1.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.fmc.v1.EditProfileActivity;
import com.fmc.v1.R;
import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.constants.CommonMethods;
import com.fmc.v1.constants.Constants;
import com.fmc.v1.data.UserData;
import com.fmc.v1.view.CircularImageView;

/**
 * Created by Nilesh on 28/06/15.
 */
public class ProfileFragment extends Fragment {


    CircularImageView imgPic;
    ImageView imgCoverPic;
    ImageView imgEdit;
    TextView txtName,txtHashTags,txtBio,txtWebsite;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile,container,false);
        imgPic = (CircularImageView) view.findViewById(R.id.imgPic);
        imgCoverPic = (ImageView) view.findViewById(R.id.imgCoverPic);
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtBio = (TextView) view.findViewById(R.id.txtBio);
        txtHashTags = (TextView) view.findViewById(R.id.txtHashTags);
        txtWebsite = (TextView) view.findViewById(R.id.txtWebsite);
        imgEdit = (ImageView) view.findViewById(R.id.imgEdit);

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
                getActivity().finish();
            }
        });
        if (FMCApplication.loggedinUserPic != null) {
            imgPic.setImageBitmap(FMCApplication.loggedinUserPic);
        }else{

            new GetProfilePic().execute(AccessToken.getCurrentAccessToken().getUserId());
        }

        if (FMCApplication.loggedinUserCoverPic != null) {
            imgCoverPic.setImageBitmap(FMCApplication.loggedinUserCoverPic);
        }else{

            new GetProfileCoverPic().execute(FMCApplication.mPreffs.getString(Constants.PREFS_COVER_IMAGE_URL, null));
        }

        UserData userData = FMCApplication.loggedinUserData;
        if (userData != null) {
            txtName.setText(  (TextUtils.isEmpty(userData.getName())? "":userData.getName() ) );
            txtHashTags.setText(  (TextUtils.isEmpty(userData.getHashtags())? "#-":userData.getHashtags() ) );
            txtBio.setText(  (TextUtils.isEmpty(userData.getBio())? "--":userData.getBio() ) );
            txtWebsite.setText(  (TextUtils.isEmpty(userData.getWebsite())? "--":userData.getWebsite() ) );
        }
        return view;
    }

    class GetProfilePic extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub

            return CommonMethods.getSmallFacebookProfilePicture(getActivity(), params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                imgPic.setImageBitmap(result);
                FMCApplication.loggedinUserPic = result;
            }
        }

    }

    class GetProfileCoverPic extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub

            return CommonMethods.getFacebookProfileCoverPicture(getActivity(), params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null && imgCoverPic != null) {
                imgCoverPic.setImageBitmap(result);
                FMCApplication.loggedinUserCoverPic = result;
            }
        }

    }
}
