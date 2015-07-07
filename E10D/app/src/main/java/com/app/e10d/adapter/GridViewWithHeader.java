package com.app.e10d.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.app.e10d.Data.ProductData;
import com.app.e10d.R;
import com.app.e10d.constants.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by Nilesh on 08/07/15.
 */
public class GridViewWithHeader extends BaseAdapter{

    ArrayList<ProductData> arrProductData;
    Context context;
    LayoutInflater inflater;
    ImageLoader loader = ImageLoader.getInstance();

    public GridViewWithHeader(Context context , ArrayList<ProductData> arrProductData) {
        this.context = context;
        this.arrProductData = arrProductData;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return arrProductData.size();
    }

    @Override
    public Object getItem(int position) {
        return arrProductData.get(position);
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
            view = inflater.inflate(R.layout.grid_cell, parent, false);
            holder = new ViewHolder();
            assert view != null;
            holder.imgProductImage = (ImageView) view.findViewById(R.id.imgProductImage);
           // holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ProductData data = (ProductData) getItem(position);
        if (data != null) {
            loader.displayImage(Constants.IMAGE_DOWNLOAD_URL + data.getPimg(), holder.imgProductImage);
            /*loader .loadImage(Constants.IMAGE_DOWNLOAD_URL+data.getPimg(),new SimpleImageLoadingListener(){

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    //super.onLoadingComplete(imageUri, view, loadedImage);
                    if (loadedImage != null) {
                        holder.imgProductImage.setImageBitmap(loadedImage);
                    }
                }
            });*/
        }

        return view;
    }

    private class ViewHolder{
        ImageView imgProductImage;
    }
}
