package com.app.e10d.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.e10d.Data.ProductData;
import com.app.e10d.R;
import com.app.e10d.constants.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
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
    DisplayImageOptions options;

    public GridViewWithHeader(Context context , ArrayList<ProductData> arrProductData) {
        this.context = context;
        this.arrProductData = arrProductData;
        inflater = LayoutInflater.from(context);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_placeholder)
                .showImageForEmptyUri(R.drawable.empty_placeholder)
                .showImageOnFail(R.drawable.empty_placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

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
            holder.txtDiscountRate = (TextView) view.findViewById(R.id.txtDiscountRate);
            holder.txtAdd = (TextView) view.findViewById(R.id.txtAdd);
            holder.txtView = (TextView) view.findViewById(R.id.txtView);
           // holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ProductData data = arrProductData.get(position);
        if (data != null) {
            ImageLoader.getInstance().displayImage(Constants.IMAGE_DOWNLOAD_URL + data.getPimg(), holder.imgProductImage, options);
            //holder.txtDiscountRate.setPaintFlags(holder.txtDiscountRate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.txtDiscountRate.setText(data.getActual_price() + "  $" + data.getDeal_price(), TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) holder.txtDiscountRate.getText();
            spannable.setSpan(new StrikethroughSpan(), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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
        TextView txtDiscountRate,txtView,txtAdd;
    }
}
