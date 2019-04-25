package com.example.pro.flickrapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
	private static final String TAG = "FlickrRecyclerViewAdapt";
	private List<Photo> mPhotoList;
	private Context mContext;

	public FlickrRecyclerViewAdapter(Context mContext, List<Photo> mPhotoList) {
		this.mContext = mContext;
		this.mPhotoList = mPhotoList;
	}

	@Override
	public FlickrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Log.d(TAG, "onCreateViewHolder: new view requested");
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
		return new FlickrImageViewHolder(view);
	}

	@Override
	public void onBindViewHolder(FlickrImageViewHolder holder, int position) {
		//Called by the layout manager when it wants new data in an existing row

		if((mPhotoList == null) || (mPhotoList.size() == 0)) {
			holder.thumbnail.setImageResource(R.drawable.placeholder);
			holder.title.setText(R.string.empty_photo);
		} else {
		Photo photoItem = mPhotoList.get(position);
		Log.d(TAG, "onBindViewHolder: " + photoItem.getmTitle() + "---->" + position);
		Picasso.get().load(photoItem.getmImage())
				.error(R.drawable.placeholder)
				.placeholder(R.drawable.placeholder)
				.into(holder.thumbnail);
		holder.title.setText(photoItem.getmTitle());
		}
	}


	@Override
	public int getItemCount() {
//		Log.d(TAG, "getItemCount: " + mPhotoList.size());
		return ((mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.size() : 1);
	}

	void loadNewData(List<Photo> newPhotos){
		mPhotoList = newPhotos;
		notifyDataSetChanged();
	}

	public Photo getPhoto(int position){
		return ((mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.get(position) : null);
	}

	class FlickrImageViewHolder extends RecyclerView.ViewHolder{
		private static final String TAG = "FlickrImageViewHolder";
		ImageView thumbnail = null;
		TextView title = null;

		public FlickrImageViewHolder(View itemView) {
			super(itemView);
			this.thumbnail = itemView.findViewById(R.id.thumbnail);
			this.title = itemView.findViewById(R.id.title);
		}
	}
}




