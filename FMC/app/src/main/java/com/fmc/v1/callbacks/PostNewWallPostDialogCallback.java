package com.fmc.v1.callbacks;

import com.fmc.v1.data.WallData;

/**
 * Created by Nilesh on 31/05/15.
 */
public interface PostNewWallPostDialogCallback {

    public void postNewWallMessage(WallData wallData);
    public void showAddNewWallPostDialog();
}
