package v1.pyroteck.com.pyroteck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import v1.pyroteck.com.pyroteck.data.Category;
import v1.pyroteck.com.pyroteck.DataHolder;
import v1.pyroteck.com.pyroteck.R;
import v1.pyroteck.com.pyroteck.callbacks.CategoryFragmentCallBacks;

/**
 * Created by Nilesh on 01/05/15.
 */
public class CategoryAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    CategoryFragmentCallBacks mCallBack;
    ArrayList<String> arrTitle = DataHolder.arrCategoryTitle;
    public CategoryAdapter(Context context, CategoryFragmentCallBacks mCallBack) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mCallBack = mCallBack;
    }

    @Override
    public int getCount() {
        return arrTitle.size();
    }

    @Override
    public Object getItem(int position) {

        return arrTitle.get(position);
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

        String title = (String) getItem(position);
        final Category category = DataHolder.mapMain.get(title);

        if(category != null){
            holder.title.setText(category.getTitle());
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Clicked " + position, Toast.LENGTH_SHORT).show();
                    mCallBack.switchToSubcategoryFragment(category.getTitle());
                }
            });
        }



        return convertView;
    }

    private class ViewHolder{
    TextView title;
    }
}
