package v1.pyroteck.com.pyroteck.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import v1.pyroteck.com.pyroteck.R;
import v1.pyroteck.com.pyroteck.callbacks.NewsCallBack;
import v1.pyroteck.com.pyroteck.data.NewsData;

/**
 * Created by Nilesh on 19/05/15.
 */
public class NewsAdapter extends BaseAdapter {

    ArrayList<NewsData> arrNewsData;
    Context context;
    LayoutInflater inflater;
    NewsCallBack mCallback;
    public NewsAdapter(Context context, ArrayList<NewsData> arrNewsData, NewsCallBack mCallback) {
        this.context = context;
        this.arrNewsData = arrNewsData;
        this.mCallback = mCallback;
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
            convertView = inflater.inflate(R.layout.news_item_cell, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
        holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
        holder.relCellContainer = (RelativeLayout) convertView.findViewById(R.id.relCellContainer);
        final NewsData newsData = (NewsData) getItem(position);

        if (newsData != null) {
            holder.txtTitle.setText(newsData.getTitle());
            if(TextUtils.isEmpty(newsData.getDetails())){
                holder.txtDescription.setVisibility(View.GONE);
            }else{
                holder.txtDescription.setVisibility(View.VISIBLE);
                holder.txtDescription.setText(newsData.getDetails());
            }

            holder.relCellContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCallback != null){
                        mCallback.switchToNewsDetailFragment(newsData.getId());
                    }
                }
            });
        }




        return convertView;
    }

    private class ViewHolder{
        TextView txtTitle;
        TextView txtDescription;
        RelativeLayout relCellContainer;
    }
}
