package com.fmc.v1.adapter;

import java.util.ArrayList;

import com.fmc.v1.R;
import com.fmc.v1.data.WallData;
import com.fmc.v1.fragments.WallFragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WallAdapter extends Adapter<WallAdapter.ViewHolder> {

	ArrayList<WallData> arrData ;
    WallFragment wallFragment;

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

        WallData data = arrData.get(position);
        if (data != null) {
            viewHolder.txtName.setText(data.getName());
            viewHolder.txtPostTime.setText(data.getTime_elapsed());
            viewHolder.txtPost.setText(data.getTextPost());
            viewHolder.txtLike.setText("Like ("+ ((data.getLikeCount() == 0)?"0":data.getLikeCount()) +")");
            viewHolder.txtCommentCount.setText( ((data.getCommentCount() == 0)?"0":data.getCommentCount()) + " Comment");


        }

		viewHolder.linLikeContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(v.getContext(), "Like Clicked. Position "+position, Toast.LENGTH_SHORT).show();
                wallFragment.postLike(arrData.get(position));
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

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_cell, parent, false);
		return new ViewHolder(v);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public TextView txtName,txtPostTime, txtPost,txtCommentCount,txtLike;
		LinearLayout linLikeContainer,linCommentContainer;
		public ViewHolder(View v) {
			super(v);
			txtName = (TextView) v.findViewById(R.id.txtName);
			txtPostTime = (TextView) v.findViewById(R.id.txtPostTime);
            txtPost = (TextView) v.findViewById(R.id.txtPost);
			txtCommentCount = (TextView) v.findViewById(R.id.txtCommentCount);
			linLikeContainer = (LinearLayout) v.findViewById(R.id.linLikeContainer);
			linCommentContainer = (LinearLayout) v.findViewById(R.id.linCommentContainer);
            txtLike = (TextView) v.findViewById(R.id.txtLike);
		}
	}

}
