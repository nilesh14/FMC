package listheaders.sample.constants;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nilesh on 19/05/15.
 */
public class Constants {

    public static final String LOGIN_URL = "http://www.referralfinder.in/api/contacts/plus-contacts.json";
    /*public static final String REGISTER_URL = "http://www.menuwiz.co/e10d-app/register.php";
    public static final String IMAGE_DOWNLOAD_URL = "http://www.menuwiz.co/e10d/images/products/"; // append the Image name to url
    public static final String LATEST_PRODUCTS_URL = "http://www.menuwiz.co/e10d-app/latest-products.php";
    public static final String PRODUCTS_URL = "http://www.menuwiz.co/e10d-app/products.php";
    public static final String PRODUCTS_DETAIL_URL = "http://www.menuwiz.co/e10d-app/product-details.php";
    public static final String ADD_TO_CART_URL = "http://www.menuwiz.co/e10d-app/add-to-cart.php";
    public static final String GET_CART_COUNT_URL = "http://www.menuwiz.co/e10d-app/cart-count.php";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "pwd";
    public static final String PREFS_ID = "prefs_id";
    public static final String PREFS_USER_NAME = "prefs_user_name";*/

    public static final int TIMEOUT_SOCKET = (int) TimeUnit.MINUTES.toMillis(2);
    public static final int TIMEOUT_CONNECTION = (int) TimeUnit.MINUTES.toMillis(2);
    public static final String GLOBAL_NOTIFICATION_CHANNEL = "global";
}
