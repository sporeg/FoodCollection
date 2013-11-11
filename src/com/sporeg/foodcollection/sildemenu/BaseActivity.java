package com.sporeg.foodcollection.sildemenu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;
import com.sporeg.foodcollection.R;

public class BaseActivity extends SherlockFragmentActivity implements
		SlidingActivityBase {
	String mode;
	int mTitleID;

	public BaseActivity(int titleID) {
		super();
		this.mTitleID = titleID;
	}

	public void finish() {
		super.finish();
		if ("2".equals(mode)) {
			overridePendingTransition(0, R.anim.activity_scroll_to_left);
		} else {
			overridePendingTransition(0, R.anim.activity_scroll_to_right);

		}

	}

	public void onCreate(Bundle paramBundle) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		mode = prefs.getString("ModePreference", "1");
		if ("2".equals(mode)) {
			overridePendingTransition(R.anim.activity_scroll_from_left, 0);
		} else {
			overridePendingTransition(R.anim.activity_scroll_from_right, 0);
		}
		super.onCreate(paramBundle);
		setTitle(mTitleID);
		ActionBar localActionBar = getSupportActionBar();
		localActionBar.setDisplayHomeAsUpEnabled(true);
		if (this.getClass().getName().equals("RandomChoise.class")) {
			localActionBar.hide();
		}

	}

	public void onPause() {
		super.onPause();
	}

	public void onResume() {
		super.onResume();
		Log.e("123"," e");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed(); // To change body of overridden methods use File
								// | Settings | File Templates.
		this.finish();
	}

	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.base_activity, menu);
		return true;
	}

	@Override
	public void setBehindContentView(View view, LayoutParams layoutParams) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBehindContentView(View view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBehindContentView(int layoutResID) {
		// TODO Auto-generated method stub

	}

	@Override
	public SlidingMenu getSlidingMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toggle() {
		// TODO Auto-generated method stub
		this.finish();
	}

	@Override
	public void showContent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showSecondaryMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSlidingActionBarEnabled(boolean slidingActionBarEnabled) {
		// TODO Auto-generated method stub

	}

}
