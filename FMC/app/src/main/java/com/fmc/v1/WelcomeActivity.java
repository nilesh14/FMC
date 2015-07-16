package com.fmc.v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.constants.Constants;

/**
 * Created by Nilesh on 19/06/15.
 */
public class WelcomeActivity extends Activity{

    TextView txtWelComeMessage;
    Button btnGetStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        txtWelComeMessage = (TextView) findViewById(R.id.txtWelComeMessage);
        btnGetStarted = (Button) findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWallActivity();
            }
        });

        txtWelComeMessage.setText(String.format( getString(R.string.welcome_to_FMC), FMCApplication.mPreffs.getString(Constants.PREFS_USER_NAME, "")) );
    }

    private void startWallActivity(){
        Toast.makeText(getApplicationContext(), "Your in!", Toast.LENGTH_LONG).show();
        if(FMCApplication.mPreffs.getBoolean(Constants.PREFS_PROFILE_SET,false)){
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            //return;
        }else{

            startActivity(new Intent(WelcomeActivity.this, EditProfileActivity.class));
        }
        finish();
    }
}
