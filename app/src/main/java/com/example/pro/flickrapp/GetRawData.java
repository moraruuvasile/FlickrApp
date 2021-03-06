package com.example.pro.flickrapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus{IDLE, PROCESSING, NOT_INITIALISED, FAILD_OR_EMTY, OK}

interface OnDownloadComplete {
	void onDownloadComplete(String data, DownloadStatus status);
}

class GetRawData extends AsyncTask<String, Void, String> {
	private static final String TAG = "GetRawData";

	private DownloadStatus mDownloadStatus;
	private final OnDownloadComplete mCallback;

	public GetRawData(OnDownloadComplete mainActivity) {
		this.mDownloadStatus = DownloadStatus.IDLE;
		this.mCallback = mainActivity;
	}

	@Override
	protected void onPostExecute(String s) {
		if(mCallback != null) mCallback.onDownloadComplete(s,mDownloadStatus);
	}

	@Override
	protected String doInBackground(String... strings) {
		HttpURLConnection connection = null;
		BufferedReader reader = null;

		if(strings == null){
			mDownloadStatus = DownloadStatus.NOT_INITIALISED;
			return null;
		}
		try{
			mDownloadStatus = DownloadStatus.PROCESSING;
			URL url = new URL(strings[0]);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int response = connection.getResponseCode();
			Log.d(TAG, "doInBackground: The response was " + response);

			StringBuilder result = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line;
			while(null != (line = reader.readLine())) result.append(line).append("\n");

			mDownloadStatus = DownloadStatus.OK;
			Log.d(TAG, "doInBackground: finish" );
			return result.toString();
		} catch (MalformedURLException e){
			Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
		} catch (IOException e){
			Log.e(TAG, "doInBackground: IO Ex reading data " + e.getMessage());
		} catch (SecurityException e){
			Log.e(TAG, "doInBackground: Needs permision " + e.getMessage());
		} finally {
			if (connection != null) connection.disconnect();

			if(reader != null){
				try {
					reader.close();
				} catch (IOException e){
					Log.e(TAG, "doInBackground: Error closing stream " + e.getMessage() );
				}
			}
		}
		mDownloadStatus = DownloadStatus.FAILD_OR_EMTY;
		return null;
	}

	void runInSameThread(String s){
//		onPostExecute(doInBackground(s));
		if(mCallback != null) mCallback.onDownloadComplete(doInBackground(s),mDownloadStatus);
	}
}
