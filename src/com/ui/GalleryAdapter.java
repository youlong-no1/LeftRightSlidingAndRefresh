package com.ui;

import java.util.List;

import com.example.androiddemo.R;
import com.ui.utils.SupportDisplay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

	public interface OnItemClickLitener {
		void onItemClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	private LayoutInflater mInflater;
	private List<Integer> mDatas;

	public GalleryAdapter(Context context, List<Integer> datats) {
		mInflater = LayoutInflater.from(context);
		mDatas = datats;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View arg0) {
			super(arg0);
		}

		ImageView ivAdimg;
		LinearLayout llBackground;
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = mInflater.inflate(R.layout.item_forum_home_ad, viewGroup, false);
		ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.llBackground = (LinearLayout) view.findViewById(R.id.item_forum_home_ad_background);
		SupportDisplay.resetAllChildViewParam(viewHolder.llBackground);
		viewHolder.ivAdimg = (ImageView) view.findViewById(R.id.item_forum_home_ad_image);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
		viewHolder.ivAdimg.setImageResource(mDatas.get(i));

		if (mOnItemClickLitener != null) {
			viewHolder.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
				}
			});

		}

	}

}
