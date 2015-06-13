package v1.pyroteck.com.pyroteck.constants;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nilesh on 19/05/15.
 */
public class Constants {

    public static final String GET_NEWS_URL = "http://menuwiz.co/ppe/news.php";
    public static final String GET_NEWS_DETAIL_URL = "http://menuwiz.co/ppe/news-details.php";
    public static final String REGISTER_USER_URL = "http://www.menuwiz.co/ppe/register-user.php";
    public static final String REGISTER_USAGE_EVENT_URL = "http://www.menuwiz.co/ppe/capture-view.php";
    public static final String UPDATE_USER_URL = "http://www.menuwiz.co/ppe/update-profile.php";

    public static final int TIMEOUT_SOCKET = (int) TimeUnit.MINUTES.toMillis(2);
    public static final int TIMEOUT_CONNECTION = (int) TimeUnit.MINUTES.toMillis(2);
    public static final String GLOBAL_NOTIFICATION_CHANNEL = "global";
}
