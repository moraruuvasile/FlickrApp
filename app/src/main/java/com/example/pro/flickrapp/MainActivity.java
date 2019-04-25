package com.example.pro.flickrapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnDataAvailable,
							RecyclerItemClickListener.OnRecyclerClickListener{
	private static final String TAG = "MainActivity";
	private FlickrRecyclerViewAdapter mFlickrRecyclerViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate: Starts");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activateToolbar(false);

		RecyclerView recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		mFlickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(this, new ArrayList<Photo>());
		recyclerView.setAdapter(mFlickrRecyclerViewAdapter);

		recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

//		GetRawData getRawData = new GetRawData(this);
//		getRawData.execute("https://api.flickr.com/services/feeds/photos_public.gne?tags=android,nougat,sdk&tagmode=any&format=json&nojsoncallback=1");

		Log.d(TAG, "onCreate: ends");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume: BBBB");

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String queryResult = sharedPreferences.getString(FLICKR_QUERY, "");

		if(queryResult.length() > 0 ){
			GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData(this,"https://api.flickr.com/services/feeds/photos_public.gne",
					"en-us", true);
			getFlickrJsonData.execute(queryResult);
//			getFlickrJsonData.executeOnSameThread(queryResult);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		if (id == R.id.action_search) {
			startActivity(new Intent(this, SearchActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

		@Override
	public void onDataAvailable(List<Photo> data, DownloadStatus status){
		if (status == DownloadStatus.OK){
			Log.d(TAG, "OnDataAvailable: data is " + data);
			mFlickrRecyclerViewAdapter.loadNewData(data);
		}
		else Log.e(TAG, "OnDataAvailable: failed " + status );
	}

	@Override
	public void onItemClick(View view, int position) {
		Toast.makeText(MainActivity.this, "Normal tap at position " + position, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemLongClick(View view, int position) {
//		Toast.makeText(MainActivity.this, "Long tap at position " + position, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, PhotoDetailActivity.class);
		intent.putExtra(PHOTO_TRANSFER, mFlickrRecyclerViewAdapter.getPhoto(position));
		startActivity(intent);
	}
}
