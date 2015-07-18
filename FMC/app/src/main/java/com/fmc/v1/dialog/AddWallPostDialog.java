package com.fmc.v1.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fmc.v1.R;
import com.fmc.v1.callbacks.PostNewWallPostDialogCallback;
import com.fmc.v1.data.WallData;

/**
 * Created by Nilesh on 31/05/15.
 */
public class AddWallPostDialog extends Dialog {

    EditText edtComment;
    Dialog dialog;
    Button btnClose,btnCommentCount;
    ImageView imgPost;
    PostNewWallPostDialogCallback postNewWallPostDialogCallback;
    public AddWallPostDialog(Context context) {
        super(context);
    }

    public AddWallPostDialog(Context context, int theme) {
        super(context, theme);
    }

    protected AddWallPostDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public AddWallPostDialog(Context context, int theme,PostNewWallPostDialogCallback postNewWallPostDialogCallback) {
        super(context, theme);
        this.postNewWallPostDialogCallback = postNewWallPostDialogCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.post_wall_dialog);
        dialog = this;

        edtComment = (EditText) findViewById(R.id.edtComment);
        imgPost = (ImageView) findViewById(R.id.imgPost);
        btnClose = (Button) findViewById(R.id.btnClose);
        btnCommentCount = (Button) findViewById(R.id.btnCommentCount);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edtComment.getText().toString())){
                    WallData data = new WallData();
                    data.setTextPost(edtComment.getText().toString());
                    postNewWallPostDialogCallback.postNewWallMessage(data);
                    dismiss();
                }else{
                    Toast.makeText(getOwnerActivity(),"Please write a post",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
