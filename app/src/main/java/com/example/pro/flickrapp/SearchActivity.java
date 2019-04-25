package com.example.pro.flickrapp;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.widget.SearchView;

public class SearchActivity extends BaseActivity  {
	private SearchView mSearchView;
	private static final String TAG = "SearchActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		activateToolbar(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_search, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		mSearchView =  (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
		SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
		mSearchView.setSearchableInfo(searchableInfo);

		mSearchView.setIconified(false);

		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				sharedPreferences.edit().putString(FLICKR_QUERY, s).apply();
				mSearchView.clearFocus();
				finish();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				return false;
			}
		});
//
		mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
			@Override
			public boolean onClose() {
				finish();
				return true;
			}
		});
		return true;
	}

}
