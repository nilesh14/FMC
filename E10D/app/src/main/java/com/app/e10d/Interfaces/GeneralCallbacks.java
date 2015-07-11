package com.app.e10d.Interfaces;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Nilesh on 07/07/15.
 */
public interface GeneralCallbacks {

    public void showErrorDialog(String title,String msg);
    public void switchFragment(int fragID , Fragment fragment);
    public void startNewActivity(Intent intent);
    public void updateCartCount(int count);
    public int getCartCountAndUpdate();
}
