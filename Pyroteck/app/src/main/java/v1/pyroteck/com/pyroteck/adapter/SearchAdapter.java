package v1.pyroteck.com.pyroteck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import v1.pyroteck.com.pyroteck.R;
import v1.pyroteck.com.pyroteck.data.SubCategory;
import v1.pyroteck.com.pyroteck.callbacks.SubCategoryCallBacks;

/**
 * Created by Nilesh on 15/05/15.
 */
public class SearchAdapter extends BaseAdapter {

    Context context;
    ArrayList<SubCategory> arrayListSubcategory;
    LayoutInflater inflater;
    SubCategoryCallBacks mCallBack;
    public SearchAdapter(Context context, ArrayList<SubCategory> arrayListSubcategory,SubCategoryCallBacks mCallBack) {
        this.context = context;
        this.arrayListSubcategory = arrayListSubcategory;
        inflater = LayoutInflater.from(context);
        this.mCallBack = mCallBack;
    }

    @Override
    public int getCount() {
        return arrayListSubcategory.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListSubcategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_cell, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title = (TextView) convertView.findViewById(R.id.txtTitle);
        final SubCategory subCategory = arrayListSubcategory.get(position);
        if (subCategory != null) {
            holder.title.setText(subCategory.getTitle());
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack != null) {
                        mCallBack.switchToDescriptionFragment(subCategory);
                    }
                }
            });
        }

        return convertView;

    }
    private class ViewHolder{
        TextView title;
    }
}
