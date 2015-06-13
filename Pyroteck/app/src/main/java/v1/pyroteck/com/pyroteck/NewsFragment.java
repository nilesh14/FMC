package v1.pyroteck.com.pyroteck;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import v1.pyroteck.com.pyroteck.adapter.NewsAdapter;
import v1.pyroteck.com.pyroteck.callbacks.NewsCallBack;
import v1.pyroteck.com.pyroteck.constants.CommonMethods;
import v1.pyroteck.com.pyroteck.constants.Constants;
import v1.pyroteck.com.pyroteck.data.NewsData;

/**
 * Created by Nilesh on 19/05/15.
 */
public class NewsFragment extends Fragment {

    private static final String TAG = "NewsFragment";
    ListView listView;
    ProgressDialog pDialog;
    ArrayList<NewsData> arrNewsData = new ArrayList<NewsData>();
    HashMap<String,String> mapData = new HashMap<String,String>();
    NewsCallBack mCallback;
    NewsAdapter newsAdapter;
    Handler mHandler;
    TextView txtEmptyMessage;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (NewsCallBack) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(getActivity());
        newsAdapter = new NewsAdapter(getActivity(),arrNewsData,mCallback);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment,container,false);
        txtEmptyMessage = (TextView) view.findViewById(R.id.txtEmptyMessage);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(newsAdapter);
        mHandler = new Handler();
        //prepareData();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(arrNewsData.size() == 0){
            mHandler.postDelayed(prepareDataRunnable, 200);
        }
        //prepareData();
    }

    Runnable prepareDataRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG,"Running PrepareData");
            prepareData();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"OnDestroy");
        mHandler.removeCallbacks(prepareDataRunnable);
        mHandler = null;
    }

    private void prepareData(){
        new GetNews(null).execute(Constants.GET_NEWS_URL);
    }

    class GetNews extends AsyncTask<String, Void, String> {

        HashMap<String, String> data;
        public GetNews(HashMap<String, String> data) {
            // TODO Auto-generated constructor stub
            this.data = data;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            if(pDialog == null){
                pDialog = new ProgressDialog(getActivity());
                //pDialog.dismiss();
            }
            pDialog.setCancelable(false);
            pDialog.setMessage("Fetching Data...");
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            return CommonMethods.callSyncService(getActivity(), params[0], TAG);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if(pDialog != null && pDialog.isShowing()){
                    pDialog.dismiss();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if(result != null){
                try {
                    parseNewsData(new JSONArray(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                showHideVoContentView();
                Toast.makeText(getActivity(), getString(R.string.no_internet_or_error), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void parseNewsData(JSONArray jArr){
        arrNewsData.clear();
        if (jArr != null) {
            for (int i = 0; i < jArr.length(); i++) {
                JSONObject jobj = jArr.optJSONObject(i);
                if (jobj != null) {
                    NewsData newsData = new NewsData();
                    newsData.setId(jobj.optString("id"));
                    newsData.setTitle(jobj.optString("title"));
                    newsData.setDetails(jobj.optString("details"));
                    arrNewsData.add(newsData);
                }
            }
        }
        if (newsAdapter != null) {
            showHideVoContentView();
            newsAdapter.notifyDataSetChanged();
        }
    }

    private void showHideVoContentView(){
        if(arrNewsData.size() == 0){
            txtEmptyMessage.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else{
            txtEmptyMessage.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

}
