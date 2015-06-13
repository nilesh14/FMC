package com.fmc.v1.callbacks;

/**
 * Created by Nilesh on 31/05/15.
 */
public interface AsyncCallBacks {

    public void onPreExecuteBackgroundTask();
    public void inBackGround(Object... params);
    public void onPostExecuteBackgroundTask(Object result);
}
