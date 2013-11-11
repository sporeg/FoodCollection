package com.sporeg.foodcollection;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class FoodPreferenceActivity extends SherlockPreferenceActivity {
	String mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		mode = prefs.getString("ModePreference", "1");
		if ("2".equals(mode)) {
			overridePendingTransition(R.anim.activity_scroll_from_left, 0);
		} else {
			overridePendingTransition(R.anim.activity_scroll_from_right, 0);
		}
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void finish() {
		super.finish();
		if ("2".equals(mode)) {
			overridePendingTransition(0, R.anim.activity_scroll_to_left);
		} else {
			overridePendingTransition(0, R.anim.activity_scroll_to_right);

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed(); // To change body of overridden methods use File
								// | Settings | File Templates.
		if ("2".equals(mode)) {
			overridePendingTransition(0, R.anim.activity_scroll_to_left);
		} else {
			overridePendingTransition(0, R.anim.activity_scroll_to_right);

		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.base_activity, menu);
		return true;
	}

}
