package com.example.pro.flickrapp;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
	private static final String TAG = "RecyclerItemClickListen";

	interface OnRecyclerClickListener{
		void onItemClick(View view, int position);
		void onItemLongClick(View view, int position);
	}

	private final OnRecyclerClickListener mListiner;
	private final GestureDetectorCompat mGestureDetector;

	public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnRecyclerClickListener listiner) {
		this.mListiner = listiner;
		mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				Log.d(TAG, "onSingleTapUp: ");
				View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
				if(mListiner != null && childView != null){
					mListiner.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
				}
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				Log.d(TAG, "onLongPress: ");
				View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
				if(mListiner != null && childView != null){
					mListiner.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
				}
			}

		});
	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
		Log.d(TAG, "onInterceptTouchEvent: XXXXXXXXXXXXXXXXXXXXXXX");
		if(mGestureDetector != null){
			boolean result = mGestureDetector.onTouchEvent(e);
			Log.d(TAG, "onInterceptTouchEvent: YYYYY" + result);
			return result;
		} else{
			Log.d(TAG, "onInterceptTouchEvent: TTYY");
			return false;
		}
	}
}
