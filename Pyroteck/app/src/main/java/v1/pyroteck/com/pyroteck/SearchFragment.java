package v1.pyroteck.com.pyroteck;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import v1.pyroteck.com.pyroteck.adapter.SearchAdapter;
import v1.pyroteck.com.pyroteck.callbacks.SubCategoryCallBacks;
import v1.pyroteck.com.pyroteck.data.Category;
import v1.pyroteck.com.pyroteck.data.SubCategory;

/**
 * Created by Nilesh on 15/05/15.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    EditText edtSearchKeyword;
    ListView listSearchResult;
    ArrayList<SubCategory> arrayList = new ArrayList<>();
    ArrayList<SubCategory> arrayAdapterList = new ArrayList<>();
    SearchAdapter adapter;
    SubCategoryCallBacks mCallBack;
    TextView txtEmptyMessage;
    public static HashMap<String,Category> mapData = new HashMap<String,Category>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        prepareSearchData();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment,container,false);
        edtSearchKeyword = (EditText) view.findViewById(R.id.edtSearchKeyword);
        listSearchResult = (ListView) view.findViewById(R.id.listSearchResult);
        txtEmptyMessage = (TextView) view.findViewById(R.id.txtEmptyMessage);

        adapter = new SearchAdapter(getActivity(),arrayAdapterList,mCallBack);
        listSearchResult.setAdapter(adapter);
        edtSearchKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleSearch(s.toString());
                adapter.notifyDataSetChanged();
                if (arrayAdapterList.size() == 0) {
                    listSearchResult.setVisibility(View.GONE);
                    txtEmptyMessage.setVisibility(View.VISIBLE);
                } else {
                    listSearchResult.setVisibility(View.VISIBLE);
                    txtEmptyMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtSearchKeyword.setText("");

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        hideSoftKeyboard(edtSearchKeyword);
    }

    private void hideSoftKeyboard(EditText editText){

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    @Override
    public void onAttach(Activity activity) {
        mCallBack = (SubCategoryCallBacks) activity;
        super.onAttach(activity);
    }

    private void handleSearch(String searchText){
        arrayAdapterList.clear();
        for (SubCategory subCategory : arrayList) {
            if(subCategory.getTitle().toLowerCase().contains(searchText.toLowerCase())){
                arrayAdapterList.add(subCategory);
            }
        }
    }

    private void prepareSearchData(){
        mapData = DataHolder.mapMain;

        if(mapData != null){
           // Map.Entry<String,Category> entry = (Map.Entry<String, Category>) mapData.entrySet();

            for (Map.Entry<String, Category> stringCategoryEntry : mapData.entrySet()) {
                if (stringCategoryEntry != null) {
                    Category category = stringCategoryEntry.getValue();
                    if (category != null) {
                        for (SubCategory subCategory : category.getArrSubcategory()) {
                            arrayList.add(subCategory);
                        }

                    }

                }
            }
        }
    }
}
