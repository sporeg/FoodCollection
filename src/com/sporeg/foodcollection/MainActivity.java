package com.sporeg.foodcollection;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sporeg.foodcollection.database.FoodColumn;
import com.sporeg.foodcollection.database.FoodProvider;
import com.sporeg.foodcollection.sildemenu.MenuFragment;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends SlidingFragmentActivity {

	protected SherlockFragment mFrag;
	Cursor cursor;
	ListView list;
	SimpleCursorAdapter adapter;
	String selection = null;
	Animation mAnimation;
//	private static final int DeleteFood_ID = Menu.FIRST;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent();
		intent.setData(FoodProvider.CONTENT_URI);
		setTitle(R.string.main_food_label);
		setContentView(R.layout.food_list);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String mode = prefs.getString("ModePreference", "1");
		Log.e("mode", mode);
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.R.color.holo_blue_dark));
		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		mFrag = new MenuFragment();
		t.replace(R.id.menu_frame, mFrag);
		t.commit();

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		if ("2".equals(mode)) {
			sm.setMode(SlidingMenu.RIGHT);
			sm.setSecondaryShadowDrawable(R.drawable.shadowright);
		} else {
			sm.setMode(SlidingMenu.LEFT);
			sm.setShadowDrawable(R.drawable.shadow);

		}

		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Spinner spinner = (Spinner) findViewById(R.id.sort);
		list = (ListView) findViewById(android.R.id.list);
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
				this, R.array.sort_choise, R.layout.spinner_dropdown_item);
		// Specify the layout to use when the list of choices appears
		adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter1);

		// // 启用长按支持，弹出的上下文菜单在
		// list.setOnCreateContextMenuListener(this);

		// 使用managedQuery获取ContactsProvider的Cursor
		cursor = initselect(FoodColumn.NAME + " COLLATE LOCALIZED");

		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				switch (arg2) {
				case 0:
					cursor = initselect(FoodColumn.NAME + " COLLATE LOCALIZED");
					break;

				case 1:
					cursor = initselect(FoodColumn.PRICE);
					break;

				case 2:
					cursor = initselect(FoodColumn.PRICE + " DESC");

					break;
				case 3:
					cursor = initselect(FoodColumn.ASSESSMENT + " DESC");

					break;

				default:
					break;
				}
				arg0.setVisibility(View.VISIBLE);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
			
			
			
		});
		mAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		spinner.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				v.startAnimation(mAnimation);
				return false;
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				Uri uri = ContentUris.withAppendedId(FoodProvider.CONTENT_URI,
						id);

				String action = getIntent().getAction();
				if (Intent.ACTION_PICK.equals(action)
						|| Intent.ACTION_GET_CONTENT.equals(action)) {

					setResult(RESULT_OK, new Intent().setData(uri));
				} else {
					// 编辑 食物
					startActivity(new Intent(Intent.ACTION_EDIT, uri));
				}
			}
		});
		handleIntent(getIntent());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		case R.id.menu_search:
			onSearchRequested();
			return true;
		case R.id.menu_add:
			startActivity(new Intent(Intent.ACTION_INSERT,
					FoodProvider.CONTENT_URI));
			return true;
			// case DeleteFood_ID: {
			// Log.e("TAG", "delete ");
			// AdapterView.AdapterContextMenuInfo info;
			// try {
			// info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			// } catch (ClassCastException e) {
			// return false;
			// }
			// Uri noteUri =
			// ContentUris.withAppendedId(FoodProvider.CONTENT_URI,
			// info.id);
			// getContentResolver().delete(noteUri, null, null);
			// return true;
			// }
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main_activity, menu);
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//			SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
//					.getActionView();
//			searchView.setSearchableInfo(searchManager
//					.getSearchableInfo(getComponentName()));
//			searchView.setIconifiedByDefault(false);
//		}
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// Because this activity has set launchMode="singleTop", the system
		// calls this method
		// to deliver the intent if this activity is currently the foreground
		// activity when
		// invoked again (when the user executes a search from this activity, we
		// don't create
		// a new instance of this activity, so the system delivers the search
		// intent here)
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// handles a search query
			String query = intent.getStringExtra(SearchManager.QUERY);
			// Log.e("search", query);
			selection = FoodColumn.NAME + " like ('%" + query + "%')";
			cursor = initsearch(selection);
		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			Log.e("saea"," " + intent.getData());
			selection = FoodColumn.NAME + " like ('%" + intent.getData()
					+ "%')";
			cursor = initsearch(selection);

		}
	}

	public Cursor initsearch(String selection) {
		Cursor cursor = getContentResolver().query(FoodProvider.CONTENT_URI,
				null, selection, null, null);
		Log.e("search", " " + cursor.getCount());
		cursor.moveToFirst();
		SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(this,
				R.layout.food_list_item, cursor, new String[] {
						FoodColumn.NAME, FoodColumn.PRICE,
						FoodColumn.ASSESSMENT }, new int[] { R.id.name,
						R.id.foodinfo1, R.id.foodinfo2 });
		// adapter = new MyCursorAdapter(this,mCursor,true);
		// 为当前ListView关联Adapter
		list.setAdapter(adapter1);
		return cursor;
	}

	public Cursor initselect(String sortby) {
		Cursor cursor1;
		if (selection == null) {
			cursor1 = getContentResolver().query(FoodProvider.CONTENT_URI,
					FoodColumn.PROJECTION, null, null, sortby);
		} else {
			cursor1 = getContentResolver().query(FoodProvider.CONTENT_URI,
					null, selection, null, sortby);
		}
		cursor1.moveToFirst();
		SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(this,
				R.layout.food_list_item, cursor1, new String[] {
						FoodColumn.NAME, FoodColumn.PRICE,
						FoodColumn.ASSESSMENT }, new int[] { R.id.name,
						R.id.foodinfo1, R.id.foodinfo2 });

		// 为当前ListView关联Adapter
		list.setAdapter(adapter1);
		return cursor1;
	}

	// // 上下文菜单，本例会通过长按条目激活上下文菜单
	// @Override
	// public void onCreateContextMenu(ContextMenu menu, View view,
	// ContextMenuInfo menuInfo) {
	//
	// AdapterView.AdapterContextMenuInfo info;
	// try {
	// info = (AdapterView.AdapterContextMenuInfo) menuInfo;
	// } catch (ClassCastException e) {
	// return;
	// }
	//
	// Cursor cursor = (Cursor) list.getAdapter().getItem(info.position);
	// if (cursor == null) {
	// return;
	// }
	//
	// menu.setHeaderTitle(cursor.getString(1));
	// android.view.MenuItem menu1 = menu.add(0, DeleteFood_ID, 0,
	// R.string.menu_delete);
	// menu1.getItemId();
	// Log.e("TAG", "" + menu1.getItemId());
	// }
	//
	// @Override
	// // 上下文菜单选择的回调函数
	// public boolean onContextMenuItemSelected(MenuItem item) {
	// Log.e("TAG", "delete " + item.getItemId());
	// AdapterView.AdapterContextMenuInfo info;
	// try {
	// info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	// } catch (ClassCastException e) {
	// return false;
	// }
	//
	// switch (item.getItemId()) {
	// case DeleteFood_ID: {
	// Log.e("TAG", "delete " + info.id);
	// Uri noteUri = ContentUris.withAppendedId(FoodProvider.CONTENT_URI,
	// info.id);
	// getContentResolver().delete(noteUri, null, null);
	// return true;
	// }
	//
	// }
	// Log.e("TAG", "delete " + item.getItemId());
	// return false;
	// }

	@Override
	public void onBackPressed() {
		if (selection != null) {
			selection = null;
			cursor = initselect(FoodColumn.NAME);
		} else {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(R.string.exit)
					.setMessage("你真的要退出了么")
					.setPositiveButton(R.string.exit,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
									android.os.Process
											.killProcess(android.os.Process
													.myPid());

								}
							}).setNegativeButton(R.string.menu_cancel, null)
					.create().show();
		}

	}

}
