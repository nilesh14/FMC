package com.fmc.v1.adapter;

import java.util.ArrayList;

import com.fmc.v1.R;
import com.fmc.v1.application.FMCApplication;
import com.fmc.v1.data.WallData;
import com.fmc.v1.fragments.WallFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rockerhieu.emojicon.EmojiconTextView;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WallAdapter extends Adapter<WallAdapter.ViewHolder> {

	ArrayList<WallData> arrData ;
    WallFragment wallFragment;
	FMCApplication app ;

	public WallAdapter(ArrayList<WallData> arrData,WallFragment wallFragment){
		this.arrData = arrData;
        this.wallFragment = wallFragment;
	}
	
	public void addWallItematPosition(WallData data, int position){
		arrData.add(position,data);
		notifyItemInserted(position);
	}

	public void addWallItem(WallData data){
		arrData.add(data);
		notifyItemInserted(arrData.size() - 1);
	}
	
	public int removeWallItem(WallData data){
        int position = -1;
		position = arrData.indexOf(data);
		arrData.remove(position);
		notifyItemRemoved(position);
        return position;
	}
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return arrData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		// TODO Auto-generated method stub


        final WallData data = arrData.get(position);
        if (data != null) {
            viewHolder.txtName.setText(data.getName());
            viewHolder.txtPostTime.setText(data.getTime_elapsed());
            viewHolder.txtPost.setText(data.getTextPost());
            viewHolder.txtLike.setText(String.valueOf(data.getLikeCount()));
            viewHolder.txtBookmark.setText(String.valueOf(data.getBookmarkCount()));
            viewHolder.txtCommentCount.setText(String.valueOf(data.getCommentCount()));
			if(!TextUtils.isEmpty(data.getImg())){
				viewHolder.imgPostImage.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(data.getImg(), viewHolder.imgPostImage, FMCApplication.wallImageOptions);
			}else {
				viewHolder.imgPostImage.setVisibility(View.GONE);
			}

			if(app == null){
				app = (FMCApplication) viewHolder.imgImage.getContext().getApplicationContext();
			}

			if(!TextUtils.isEmpty(data.getFb_id())){
				ImageLoader.getInstance().displayImage(app.makeFacebookProfileURL(data.getFb_id()), viewHolder.imgImage, FMCApplication.options);
			}

			if(data.isPostLiked()){
				viewHolder.imgLikeImage.setImageResource(R.drawable.heart_wall_cell_selected);
			}else{
				viewHolder.imgLikeImage.setImageResource(R.drawable.heart_wall_cell);
			}

			if(data.isPostBookmarked()){
				viewHolder.imgBookmarkImage.setImageResource(R.drawable.bookmark_selected);
			}else{
				viewHolder.imgBookmarkImage.setImageResource(R.drawable.bookmark);
			}

			viewHolder.linLikeContainer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast.makeText(v.getContext(), "Like Clicked. Position "+position, Toast.LENGTH_SHORT).show();
					if(!data.isPostLiked()){
						wallFragment.postLike(arrData.get(position));
						//data.setPostLiked(!data.isPostLiked());
					}
				}
			});

			viewHolder.linBookmarkContainer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast.makeText(v.getContext(), "Comment Clicked", Toast.LENGTH_SHORT).show();
					if(!data.isPostBookmarked()){
						wallFragment.postBookmark(arrData.get(position));
					}
				}
			});
			viewHolder.linCommentContainer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast.makeText(v.getContext(), "Comment Clicked", Toast.LENGTH_SHORT).show();

					wallFragment.postComment(arrData.get(position));

				}
			});

		}


	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_cell, parent, false);
		return new ViewHolder(v);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public TextView txtName,txtPostTime, txtCommentCount,txtLike,txtBookmark;
		EmojiconTextView txtPost;
		LinearLayout linLikeContainer,linCommentContainer,linBookmarkContainer;
		ImageView imgPostImage,imgImage,imgLikeImage,imgBookmarkImage;
		public ViewHolder(View v) {
			super(v);
			txtName = (TextView) v.findViewById(R.id.txtName);
			txtName.setTypeface(FMCApplication.ubuntu);
			txtPostTime = (TextView) v.findViewById(R.id.txtPostTime);
			txtPostTime.setTypeface(FMCApplication.ubuntu);
            txtPost = (EmojiconTextView) v.findViewById(R.id.txtPost);
			txtPost.setTypeface(FMCApplication.ubuntu);
			txtCommentCount = (TextView) v.findViewById(R.id.txtCommentCount);
			txtCommentCount.setTypeface(FMCApplication.ubuntu);
			linLikeContainer = (LinearLayout) v.findViewById(R.id.linLikeContainer);
			linCommentContainer = (LinearLayout) v.findViewById(R.id.linCommentContainer);
			linBookmarkContainer = (LinearLayout) v.findViewById(R.id.linBookmarkContainer);
            txtLike = (TextView) v.findViewById(R.id.txtLike);
			txtLike.setTypeface(FMCApplication.ubuntu);
			txtBookmark = (TextView) v.findViewById(R.id.txtBookmark);
			txtBookmark.setTypeface(FMCApplication.ubuntu);
			imgPostImage = (ImageView) v.findViewById(R.id.imgPostImage);
			imgImage = (ImageView) v.findViewById(R.id.imgImage);
			imgLikeImage = (ImageView) v.findViewById(R.id.imgLikeImage);
			imgBookmarkImage = (ImageView) v.findViewById(R.id.imgBookmarkImage);
		}
	}

}
