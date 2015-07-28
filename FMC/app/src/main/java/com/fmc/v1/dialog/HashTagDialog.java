package com.fmc.v1.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fmc.v1.R;
import com.fmc.v1.adapter.HashTagAdapter;
import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.data.HashtagData;

import java.util.ArrayList;

/**
 * Created by Nilesh on 29/07/15.
 */
public class HashTagDialog extends Dialog {

    ArrayList<HashtagData> arrData ;
    ListView listHashTags;
    TextView txtHeaderText;
    ImageView imgClose,imgPost;

    public HashTagDialog(Context context) {
        super(context);
    }

    public HashTagDialog(Context context, int theme) {
        super(context, theme);
    }

    protected HashTagDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.hashtag_selection_dialog);

        listHashTags = (ListView) findViewById(R.id.listHashTags);
        txtHeaderText = (TextView) findViewById(R.id.txtHeaderText);
        txtHeaderText.setTypeface(FMCApplication.ubuntu);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        imgPost = (ImageView) findViewById(R.id.imgPost);
        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrData = null;
                dismiss();
            }
        });

        if(arrData.size() == 0){
            prepareHashtagData();
        }
        try {
            HashTagAdapter adapter = new HashTagAdapter(getContext(),arrData);
            listHashTags.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<HashtagData> getArrData() {
        return arrData;
    }

    public void setArrData(ArrayList<HashtagData> arrData) {
        this.arrData = arrData;
    }

    private void prepareHashtagData(){
        arrData.clear();
        /*for(int i = 0; i < 6; i ++){
            HashtagData data = new HashtagData();
            data.setText("Tag Number "+i);
            arrData.add(data);
        }*/
    }
}
