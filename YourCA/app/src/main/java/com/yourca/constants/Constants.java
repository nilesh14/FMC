package com.yourca.constants;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nilesh on 19/05/15.
 */
public class Constants {

    public static final String LOGIN_URL = "http://www.menuwiz.co/ca-app/login.php";
    public static final String ADD_ENTRY_URL = "http://www.menuwiz.co/ca-app/add-entry.php";
    public static final String ENTRY_LIST_URL = "http://www.menuwiz.co/ca-app/list.php";
    public static final String VIEW_ENTRY_URL = "http://www.menuwiz.co/ca-app/list-details.php";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "pwd";
    public static final String PREFS_ID = "prefs_id";
    public static final String PREFS_USER_ID = "prefs_user_id";

    public static final int TIMEOUT_SOCKET = (int) TimeUnit.MINUTES.toMillis(2);
    public static final int TIMEOUT_CONNECTION = (int) TimeUnit.MINUTES.toMillis(2);
    public static final String GLOBAL_NOTIFICATION_CHANNEL = "global";
}
