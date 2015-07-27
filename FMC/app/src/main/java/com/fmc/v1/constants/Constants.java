package com.fmc.v1.constants;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nilesh on 10/05/15.
 */
public class Constants {

    public static String USER_NAME ="";
    public static String USER_EMAIL ="";
    public static String USER_GENDER ="";
    public static String PREFS_NAME ="com.fmc.v1";
    public static String PREFS_USER_NAME ="user_name";
    public static String PREFS_USER_GENDER ="user_gender";
    public static String PREFS_USER_EMAIL ="user_email";
    public static String PREFS_USER_LOCATION ="user_location";
    public static String PREFS_COVER_IMAGE_URL ="cover_image_URL";
    public static String PREFS_GLOBAL_SELECTED ="is_local_selected";
    public static String PREFS_USER_ID ="user_id";
    public static String PREFS_UID ="uid";
    public static String PREFS_HASHTAGS ="hashtags_prefs";
    public static String PREFS_BIO ="bio_prefs";
    public static String PREFS_WEBSITE ="website_prefs";
    public static String PREFS_BIRTHDAY ="birthday_prefs";
    public static String PREFS_BITCH_USERNAME ="bitch_username_prefs";
    public static String PREFS_BITCH_PASSWORD ="bitch_password_prefs";
    public static String PREFS_CHILD_DETAIL ="child_detail_prefs";
    public static String PREFS_CODE_VALIDATION_DONE ="code_validation_done";
    public static String PREFS_PROFILE_SET ="is_profile_set";

    public static String KEY_EMAIL ="email";
    public static String KEY_VALUE ="val";
    public static String KEY_NAME ="name";
    public static String KEY_CITY ="city";
    public static String KEY_POST_ID ="post_id";
    public static String KEY_POST ="post";
    public static String KEY_LIKES ="likes";
    public static String KEY_COMMENTS ="comments";
    public static String KEY_TIME_ELAPSED ="time_elapsed";

    public static String GET_VALIDATION_CODE_URL = "http://www.firstmomsclub.in/apis/get-code.php";
    public static String GET_ALL_WALL_DATA_URL = "http://www.firstmomsclub.in/apis/wall-fetch.php";
    public static String LIKE_POST_URL = "http://www.firstmomsclub.in/apis/like-post.php";
    public static String COMMENT_URL = "http://www.firstmomsclub.in/apis/comment-post.php";
    public static String ADD_POST__URL = "http://www.firstmomsclub.in/apis/add-post.php";

    public static final int TIMEOUT_SOCKET = (int) TimeUnit.MINUTES.toMillis(1);
	public static final int TIMEOUT_CONNECTION = (int) TimeUnit.MINUTES.toMillis(1);

}
