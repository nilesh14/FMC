package v1.pyroteck.com.pyroteck.constants;

/**
 * Created by Nilesh on 19/05/15.
 */

        import java.io.BufferedInputStream;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.io.PrintWriter;
        import java.io.StringWriter;
        import java.io.Writer;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.concurrent.ConcurrentHashMap;
        import java.util.concurrent.TimeUnit;

        import org.apache.http.HttpResponse;
        import org.apache.http.NameValuePair;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.conn.ConnectTimeoutException;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;
        import org.apache.http.params.BasicHttpParams;
        import org.apache.http.params.HttpConnectionParams;
        import org.apache.http.params.HttpParams;
        import org.apache.http.util.EntityUtils;
        import org.json.JSONException;
        import org.json.JSONObject;

        import android.app.ActivityManager;
        import android.app.ActivityManager.RunningTaskInfo;
        import android.app.Dialog;
        import android.content.ComponentName;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.content.res.Resources;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Typeface;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.net.NetworkInfo.State;
        import android.net.wifi.WifiManager;
        import android.provider.Settings.Secure;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.util.TypedValue;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.Toast;


public class CommonMethods {

    private static final String Tag = "CommonMethods";

    public static String getUniqueDeviceID(Context ctx){
        String android_id = Secure.getString(ctx.getContentResolver(),
                Secure.ANDROID_ID);
        return android_id;
    }

    public static Bitmap getFacebookProfilePicture(Context context,String userID){
        if(!CommonMethods.isNetworkPresent(context)){
            Toast.makeText(context, "No Network Present", Toast.LENGTH_SHORT).show();
            return null;
        }
        URL imageURL;
        Bitmap bitmap = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");


            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bitmap;
    }

    public static String callSyncService(Context context,String webservice_url,String TAG) {

        if(!CommonMethods.isNetworkPresent(context)){
            //Toast.makeText(context, "No Network Present", Toast.LENGTH_SHORT).show();
            return "";
        }

        String resultString = "0";
        //Log.d(TAG, " json Value "+jsonString);
        try
        {
            //HttpPost httppost = new HttpPost(webservice_url);
            HttpGet httpGet = new HttpGet(webservice_url);

            //passing as parameter to webservice as request and then get responsse from there.
            //			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //			nameValuePairs.add(new BasicNameValuePair("json",
            //					jsonString ));
            //
            //			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            //int timeoutConnection = 100000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, Constants.TIMEOUT_CONNECTION);
            //int timeoutSocket = (int) TimeUnit.SECONDS.toMillis(2);
            HttpConnectionParams.setSoTimeout(httpParameters, Constants.TIMEOUT_SOCKET);


            HttpClient httpclient = new DefaultHttpClient(httpParameters);

            // Execute HTTP Post Request

            HttpResponse response = httpclient.execute(httpGet);


            resultString = EntityUtils.toString(response.getEntity());
            Log.d(TAG, "Server Response "+resultString);
            Log.d(TAG, "This is the Status Line \n "+response.getStatusLine());
            Log.d(TAG, "This is the Status code \n "+response.getStatusLine().getStatusCode());

        }
        catch(ConnectTimeoutException e)
        {
            Log.d(TAG, "Connection Timeout===>");

            e.printStackTrace();
            return null;
        }
        catch (Exception e)
        {
            Log.d(TAG, "Exception ");
            e.printStackTrace();
        }


        return resultString;
    }

    public static String callSyncService(Context context,HashMap<String, String> data,String webservice_url,String TAG) {

        if(!CommonMethods.isNetworkPresent(context)){
            //Toast.makeText(context.getApplicationContext(), "No Network Present", Toast.LENGTH_SHORT).show();
            return null;
        }

        String resultString = "0";
        //Log.d(TAG, " json Value "+jsonString);
        try{
            //HttpPost httppost = new HttpPost(webservice_url);
            HttpPost httpPost = new HttpPost(webservice_url);

            //passing as parameter to webservice as request and then get responsse from there.
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            if(data != null){

                for (Map.Entry<String, String> entry : data.entrySet()){

                    System.out.println(entry.getKey() + "/" + entry.getValue());
                    nameValuePairs.add(new BasicNameValuePair(entry.getKey(),
                            entry.getValue() ));
                }
            }

            //			nameValuePairs.add(new BasicNameValuePair("json",
            //					jsonString ));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            //int timeoutConnection = 100000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, Constants.TIMEOUT_CONNECTION);
            //int timeoutSocket = (int) TimeUnit.SECONDS.toMillis(2);
            HttpConnectionParams.setSoTimeout(httpParameters, Constants.TIMEOUT_SOCKET);


            HttpClient httpclient = new DefaultHttpClient(httpParameters);

            // Execute HTTP Post Request

            HttpResponse response = httpclient.execute(httpPost);


            resultString = EntityUtils.toString(response.getEntity());
            Log.d(TAG, "Server Response "+resultString);
            Log.d(TAG, "This is the Status Line \n "+response.getStatusLine());
            Log.d(TAG, "This is the Status code \n "+response.getStatusLine().getStatusCode());

        }
        catch(ConnectTimeoutException e)
        {
            Log.d(TAG, "Connection Timeout===>");
            e.printStackTrace();
        }
        catch (Exception e)
        {
            Log.d(TAG, "Exception ");
            e.printStackTrace();
        }


        return resultString;
    }

    public static boolean isNetworkPresent(Context context) {
        boolean isNetworkAvailable = false;
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);


            try {

                if (cm != null) {
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null) {

                        isNetworkAvailable = netInfo.isConnected();
                        //                    Toast.makeText(context, "Connecting...", Toast.LENGTH_SHORT).show();
                        //Log.d("NETWORK<<","Connecting...."+netInfo.getReason());
                    }
                }
            } catch (Exception ex) {
                //Log.e("Network Avail Error", ex.getMessage());

            }
            //        check for wifi also
            if(!isNetworkAvailable){

                WifiManager connec = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

                State wifi = cm.getNetworkInfo(1).getState();
                if (connec.isWifiEnabled()
                        && wifi.toString().equalsIgnoreCase("CONNECTED")) {
                    isNetworkAvailable = true;
                    //Log.d("WIFI NETWORK<<","WIFI connected");
                } else {

                    isNetworkAvailable = false;
                    // Log.d("WIFI Network<<","WIFI not connected");
                }

            }
        }

        return isNetworkAvailable;

    }


    public float convertDIPtoPixel(Context ctx,float value){
        Resources r = ctx.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());

        return px;

    }



    public static void writeTextFile(String key,String data){
        try {
            Log.d(key, data);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            Date currentDate = new Date();
            try {
                currentDate=sdf.parse(sdf.format(currentDate));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                CommonMethods.writeTextFile(Tag,"  "+getStackTrace(e));
            }
            String path="/sdcard/gpstrackerlogfile.txt";
            File myFile = new File(path);
            if(!myFile.exists()){
                /**
                 * android.os.Build.VERSION.SDK      // API Level
                 android.os.Build.DEVICE           // Device
                 android.os.Build.MODEL            // Model
                 android.os.Build.PRODUCT
                 */
                Log.d("CommonMethods", "Writing the Device info");
                myFile.createNewFile();
                FileWriter fw = new FileWriter(myFile,true);
                fw.write("API Level : "+android.os.Build.VERSION.SDK_INT+"\r\n");
                fw.write("Device : "+android.os.Build.DEVICE+"\r\n");//appends the string to the file
                fw.write("Model : "+android.os.Build.MODEL+"\r\n");
                fw.write("Product : "+android.os.Build.PRODUCT+"\r\n");
                fw.write("Manufacturer : "+android.os.Build.MANUFACTURER+"\r\n");
                fw.flush();
                fw.close();
            }
            myFile.createNewFile();
            FileWriter fw = new FileWriter(myFile,true);
            fw.write("Time "+sdf.format(currentDate)+"\r\t  "+key+" \r\t "+data +"\r\n");//appends the string to the file
            fw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("Custom Stack trace ", getStackTrace(e));
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Custom Stack trace ", getStackTrace(e));
        }
    }
    public static String getStackTrace(Throwable aThrowable) {
        aThrowable.printStackTrace();
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

}

