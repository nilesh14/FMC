package v1.pyroteck.com.pyroteck;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import v1.pyroteck.com.pyroteck.constants.CommonMethods;
import v1.pyroteck.com.pyroteck.constants.Constants;
import v1.pyroteck.com.pyroteck.data.NewsData;

/**
 * Created by Nilesh on 19/05/15.
 */
public class NewsDetailFragment extends Fragment {

    private static final String TAG = "NewsDetailFragment";
    TextView txtTitle,txtDescription;
    ProgressDialog pDialog;
    NewsData newsData = new NewsData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_detail_layout,container,false);
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtDescription = (TextView) view.findViewById(R.id.txtDescription);

        Bundle bundle = getArguments();
        String id = bundle.getString("id");
        HashMap<String,String> mapData = new HashMap<>();
        mapData.put("id",id);
        new GetNews(mapData).execute(Constants.GET_NEWS_DETAIL_URL);
        return view;
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
                pDialog.dismiss();
            }
            pDialog.setCancelable(false);
            pDialog.setMessage("Fetching Details Data...");
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            return CommonMethods.callSyncService(getActivity(), data, params[0], TAG);
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
                Toast.makeText(getActivity(), getString(R.string.no_internet_or_error), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void parseNewsData(JSONArray jsonArray) {

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jobj = jsonArray.optJSONObject(i);
                if (jobj != null) {
                    newsData.setId(jobj.optString("id"));
                    newsData.setTitle(jobj.optString("title"));
                    newsData.setDetails(jobj.optString("details"));

                }
            }

            txtTitle.setText(newsData.getTitle());
            txtDescription.setText(newsData.getDetails());
        }
    }

}
