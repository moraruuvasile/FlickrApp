package com.example.pro.flickrapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

interface OnDataAvailable {
	void onDataAvailable(List<Photo> data, DownloadStatus status);
}

class GetFlickrJsonData extends AsyncTask<String,Void,List<Photo>> implements OnDownloadComplete {
	private static final String TAG = "GetFlickrJsonData";
	private List<Photo> mPhotoList = null;
	private String mBaseURL;
	private String mLanguage;
	private boolean mMatchAll;
	private final OnDataAvailable mCallBack;
	private boolean runingOnSameThread = false;

	public GetFlickrJsonData( OnDataAvailable mCallBack, String mBaseURL, String mLanguage, boolean mMatchAll) {
		this.mBaseURL = mBaseURL;
		this.mLanguage = mLanguage;
		this.mMatchAll = mMatchAll;
		this.mCallBack = mCallBack;
	}

	void executeOnSameThread(String searchCriteria) {
		runingOnSameThread = true;
		String destinationURI = createURI(searchCriteria, mLanguage, mMatchAll);
		GetRawData getRawData = new GetRawData(this);
		getRawData.execute(destinationURI);
	}

	@Override
	protected void onPostExecute(List<Photo> photos) {
		if (mCallBack != null) mCallBack.onDataAvailable(mPhotoList,DownloadStatus.OK);
	}

	@Override
	protected List<Photo> doInBackground(String... params) {
		Log.d(TAG, "doInBackground: CCCCCC");
		String destinationURI = createURI(params[0], mLanguage, mMatchAll);
		Log.d(TAG, "doInBackground: " + destinationURI);
		GetRawData getRawData = new GetRawData(this);
		getRawData.runInSameThread(destinationURI);
		Log.d(TAG, "doInBackground: DDDDDDD");
		return mPhotoList;
	}

	private String createURI(String searchCriteria, String lang, boolean mMatchAll){

//		Uri uri = Uri.parse(mBaseURL);
//		Uri.Builder builder = uri.buildUpon();
//		builder = builder.appendQueryParameter("tags", searchCriteria);
//		builder = builder.appendQueryParameter("tagmode", mMatchAll ? "ALL" : "ANY");
//		builder = builder.appendQueryParameter("lang", lang);
//		builder = builder.appendQueryParameter("format", "json");
//		builder = builder.appendQueryParameter("nojsoncallback", "1");
//		uri = builder.build();

		return Uri.parse(mBaseURL).buildUpon()
				.appendQueryParameter("tags", searchCriteria)
				.appendQueryParameter("tagmode", mMatchAll ? "ALL" : "ANY")
				.appendQueryParameter("lang", lang)
				.appendQueryParameter("format", "json")
				.appendQueryParameter("nojsoncallback", "1")
				.build().toString();
	}

	@Override
	public void onDownloadComplete(String data, DownloadStatus status) {
		if(status == DownloadStatus.OK){
			mPhotoList = new ArrayList<>();
			try{
				JSONObject jsonData = new JSONObject(data);
				JSONArray itemsArray = jsonData.getJSONArray("items");

				for (int i = 0; i < itemsArray.length() ; i++) {
					JSONObject jsonPhoto = itemsArray.getJSONObject(i);
					String title = jsonPhoto.getString("title");
					String author = jsonPhoto.getString("author");
					String authorID = jsonPhoto.getString("author_id");
					String tags = jsonPhoto.getString("tags");

					JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
					String photoUrl = jsonMedia.getString("m");
					String link = photoUrl.replaceFirst("_m.", "_b.");

					Photo photo = new Photo(title,author,authorID,link,tags,photoUrl);
					mPhotoList.add(photo);
				}
			} catch (JSONException jsone){
				jsone.printStackTrace();
				Log.e(TAG, "onDownloadComplete: " + jsone.getMessage());
				status = DownloadStatus.FAILD_OR_EMTY;
			}
		}
		if(runingOnSameThread && mCallBack != null){
			mCallBack.onDataAvailable(mPhotoList, status);
		}
	}
}
