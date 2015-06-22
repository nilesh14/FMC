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

import com.fmc.v1.R;
import com.fmc.v1.callbacks.PostNewCommentDialogCallback;
import com.fmc.v1.callbacks.PostNewWallPostDialogCallback;
import com.fmc.v1.data.WallData;
import com.fmc.v1.fragments.WallFragment;

/**
 * Created by Nilesh on 31/05/15.
 */
public class PostCommentDialog extends Dialog {

    EditText edtComment;
    Dialog dialog;
    Button btnClose,btnCommentCount;
    ImageView imgPost;
    PostNewCommentDialogCallback postNewCommentDialogCallback;
    WallData wallData;

    public PostCommentDialog(Context context, int theme) {
        super(context, theme);
    }

    protected PostCommentDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public PostCommentDialog(Context context, int theme, PostNewCommentDialogCallback postNewCommentDialogCallback,WallData wallData) {
        super(context, theme);
        this.postNewCommentDialogCallback = postNewCommentDialogCallback;
        this.wallData = wallData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.comment_dialog);
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

                    wallData.setCommentText(edtComment.getText().toString());
                    postNewCommentDialogCallback.postNewComment(wallData);
                    dismiss();
                }
            }
        });

    }
}