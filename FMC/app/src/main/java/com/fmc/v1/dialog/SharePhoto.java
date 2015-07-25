package com.fmc.v1.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fmc.v1.R;

/**
 * Created by Nilesh on 25/07/15.
 */
public class SharePhoto extends Dialog {


    EditText edtComment;
    ImageView imgImage;
    Button btnSend;
    Bitmap image;

    public SharePhoto(Context context) {
        super(context);
    }

    public SharePhoto(Context context, int theme) {
        super(context, theme);
    }

    protected SharePhoto(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.share_photo_dialog);

        edtComment = (EditText) findViewById(R.id.edtComment);
        imgImage = (ImageView) findViewById(R.id.imgImage);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getOwnerActivity(), "Image will be sent...", Toast.LENGTH_SHORT).show();

                dismiss();
            }
        });

        if (image != null) {
            imgImage.setImageBitmap(image);
        }

    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
