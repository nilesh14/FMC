package com.nile.vcardsample;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Nilesh on 19/05/15.
 */
public class DataAdapter extends BaseAdapter {

    ArrayList<Data> arrNewsData;
    Context context;
    LayoutInflater inflater;
    static String TYPE_HOME = "Home";
    static String TYPE_WORK = "Work";
    static String TYPE_MOBILE = "Mobile";
    static String TYPE_FAX_HOME = "Fax";
    static String TYPE_FAX_WORK = "Fax";
    static String TYPE_OTHER = "OTHER";
    static String TYPE_WORK_MOBILE = "Work";

    public DataAdapter(Context context, ArrayList<Data> arrNewsData) {
        this.context = context;
        this.arrNewsData = arrNewsData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrNewsData.size();
    }

    @Override
    public Object getItem(int position) {
        return arrNewsData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.data_cell, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtType = (TextView) convertView.findViewById(R.id.txtType);
        holder.txtData = (TextView) convertView.findViewById(R.id.txtData);
        holder.chkBox = (CheckBox) convertView.findViewById(R.id.chkBox);

        final Data data = (Data) getItem(position);
        if (data != null) {
            holder.chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    data.setIsChecked(isChecked);
                }
            });
            holder.chkBox.setChecked(data.isChecked);
            if (data.data2 != null && data.dataType.equalsIgnoreCase(MainActivity.DATA_TYPE_PHONE)) {
                holder.txtType.setText(getPhoneType(Integer.parseInt(data.data2)));
            }

            if (data.data2 != null && data.dataType.equalsIgnoreCase(MainActivity.DATA_TYPE_EMAIL)) {
                holder.txtType.setText(getEmailType(Integer.parseInt(data.data2)));
            }

            holder.txtData.setText(data.data1);
        }


        return convertView;
    }

    private class ViewHolder{
        TextView txtType;
        TextView txtData;
        CheckBox chkBox;
    }

    public static String getPhoneType(int type){
        String result = TYPE_OTHER;

        switch (type){
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                result = TYPE_HOME;
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                result = TYPE_MOBILE;
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                result = TYPE_WORK_MOBILE;
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                result = TYPE_WORK;
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                result = TYPE_FAX_WORK;
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                result = TYPE_FAX_HOME;
                break;
        }
        return result;
    }

    public static String getEmailType(int type){
        String result = TYPE_OTHER;

        switch (type){
            case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                result = TYPE_HOME;
                break;
            case ContactsContract.CommonDataKinds.Email.TYPE_MOBILE:
                result = TYPE_MOBILE;
                break;
            case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                result = TYPE_WORK;
                break;
            case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:
                result = TYPE_OTHER;
                break;
        }
        return result;
    }
}
