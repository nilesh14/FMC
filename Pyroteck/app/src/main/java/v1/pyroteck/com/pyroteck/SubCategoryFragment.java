package v1.pyroteck.com.pyroteck;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import v1.pyroteck.com.pyroteck.adapter.SubCategoryAdapter;
import v1.pyroteck.com.pyroteck.callbacks.SubCategoryCallBacks;
import v1.pyroteck.com.pyroteck.data.Category;

/**
 * Created by Nilesh on 01/05/15.
 */
public class SubCategoryFragment extends Fragment {


    private static final String TAG = "SubCategoryFragment";

    ListView list;
    TextView txtTitle;
    SubCategoryAdapter subCategoryAdapter;
    SubCategoryCallBacks mCallBack;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment_layout,container,false);

        Bundle bundle = getArguments();
        Log.d(TAG, "Got String " + bundle.getString("dataString"));

        list = (ListView) view.findViewById(R.id.list);
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);

        txtTitle.setText(bundle.getString("dataString"));
        txtTitle.setVisibility(View.VISIBLE);
        Category cateGory = DataHolder.mapMain.get(bundle.getString("dataString"));
        subCategoryAdapter = new SubCategoryAdapter(getActivity(),cateGory.getArrSubcategory(),mCallBack);
        list.setAdapter(subCategoryAdapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mCallBack = (SubCategoryCallBacks) activity;
    }
}
