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
 * Created by Nilesh on 01/05/15.
 */
public class SubCategoryAdapter extends BaseAdapter {

    ArrayList<SubCategory> arrSubCategory;
    Context context;
    LayoutInflater inflater;
    SubCategoryCallBacks mCallBack;
    public SubCategoryAdapter(Context context, ArrayList<SubCategory> arrSubCategory, SubCategoryCallBacks mCallBack) {
        this.arrSubCategory = arrSubCategory;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mCallBack = mCallBack;
    }

    @Override
    public int getCount() {
        return arrSubCategory.size();
    }

    @Override
    public Object getItem(int position) {
        return arrSubCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_cell, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title = (TextView) convertView.findViewById(R.id.txtTitle);
        final SubCategory subCategory = (SubCategory) getItem(position);
        if(subCategory != null){
            holder.title.setText(subCategory.getTitle());
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallBack.switchToDescriptionFragment(subCategory);
                    //Toast.makeText(context,"Clicked "+position,Toast.LENGTH_SHORT).show();
                }
            });
        }


        return convertView;
    }

    private class ViewHolder{
        TextView title;
    }
}
