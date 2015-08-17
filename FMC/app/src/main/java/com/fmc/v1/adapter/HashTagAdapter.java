package com.fmc.v1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fmc.v1.R;
import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.data.HashtagData;

import java.util.ArrayList;

/**
 * Created by Nilesh on 29/07/15.
 */
public class HashTagAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashtagData> arrData;
    LayoutInflater inflater;

    public HashTagAdapter(Context context, ArrayList<HashtagData> arrData) {
        this.context = context;
        this.arrData = arrData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrData.size();
    }

    @Override
    public Object getItem(int position) {
        return arrData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.hashtag_cell, parent, false);
            holder = new ViewHolder();
            //assert view != null;

            holder.txtHashTags = (TextView) view.findViewById(R.id.txtHashTags);
            holder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            holder.linDataContainer = (LinearLayout) view.findViewById(R.id.linDataContainer);
            // holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final HashtagData data = (HashtagData) getItem(position);

        if (data != null) {
            holder.checkbox.setChecked(data.isSelected());
            holder.txtHashTags.setText(data.getText());
            holder.txtHashTags.setTypeface(FMCApplication.ubuntu_bold);

            holder.linDataContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkbox.setChecked(!holder.checkbox.isChecked());
                    data.setIsSelected(holder.checkbox.isChecked());

                }
            });
        } else {
            holder.checkbox.setChecked(false);
        }

        return view;
    }

    private class ViewHolder {
        LinearLayout linDataContainer;
        CheckBox checkbox;
        TextView txtHashTags;
    }
}
