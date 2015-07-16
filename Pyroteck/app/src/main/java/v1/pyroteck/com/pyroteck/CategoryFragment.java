package v1.pyroteck.com.pyroteck;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import v1.pyroteck.com.pyroteck.adapter.CategoryAdapter;
import v1.pyroteck.com.pyroteck.callbacks.CategoryFragmentCallBacks;

/**
 * Created by Nilesh on 01/05/15.
 */
public class CategoryFragment extends Fragment {

    ListView list;
    CategoryAdapter adapter;
    CategoryFragmentCallBacks mCallBack;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment_layout,container,false);
        list = (ListView) view.findViewById(R.id.list);
        adapter = new CategoryAdapter(getActivity(),mCallBack);

        list.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallBack = (CategoryFragmentCallBacks) activity;

    }
}
