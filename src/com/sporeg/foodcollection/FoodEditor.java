package com.sporeg.foodcollection;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.sporeg.foodcollection.database.FoodColumn;
import com.sporeg.foodcollection.sildemenu.BaseActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FoodEditor extends BaseActivity {

	public FoodEditor() {
		super(R.string.menu_save);
		// TODO Auto-generated constructor stub
	}

	private static final String TAG = "FoodEditor";

	private static final int STATE_EDIT = 0;
	private static final int STATE_INSERT = 1;

	private int mState;
	private Uri mUri;
	private Cursor mCursor;
	private EditText nameText;
	private EditText locationText;
	private EditText priceText;
	private EditText assessmentText;
	private EditText recommendationText;
	private EditText businesshoursText;
	private EditText remarksText;

	private String originalNameText = "";
	private String originalLocationText = "";
	private String originalPriceText = "";
	private String originalAssessmentText = "";
	private String originalRecommendationText = "";
	private String originalBusinesshoursText = "";
	private String originalRemarksText = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		final String action = intent.getAction();
		Log.e(TAG + ":onCreate", action);
		if (Intent.ACTION_EDIT.equals(action)) {
			mState = STATE_EDIT;
			mUri = intent.getData();
			setTitle(R.string.menu_save);
			Log.e(TAG, mUri.toString());
		} else if (Intent.ACTION_INSERT.equals(action)) {
			mState = STATE_INSERT;
			mUri = getContentResolver().insert(intent.getData(), null);
			setTitle(R.string.food_create);
			if (mUri == null) {
				Log.e(TAG + ":onCreate", "Failed to insert new Food into "
						+ getIntent().getData());
				finish();
				return;
			}
			setResult(RESULT_OK, (new Intent()).setAction(mUri.toString()));

		} else {
			Log.e(TAG + ":onCreate", " unknown action");
			finish();
			return;
		}
		setContentView(R.layout.food_editor);
		nameText = (EditText) findViewById(R.id.name_edit);
		locationText = (EditText) findViewById(R.id.location_edit);
		priceText = (EditText) findViewById(R.id.price_edit);
		assessmentText = (EditText) findViewById(R.id.assessment_edit);
		recommendationText = (EditText) findViewById(R.id.recommendation_edit);
		businesshoursText = (EditText) findViewById(R.id.businesshours_edit);
		remarksText = (EditText) findViewById(R.id.remarks_edit);

//		phone.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
//						+ remarksText.getText()));
//				startActivity(intent);
//			}
//		});

		// saveButton.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// String text = nameText.getText().toString();
		// if (text.length() == 0) {
		// setResult(RESULT_CANCELED);
		// deleteFood();
		// finish();
		// } else {
		// updateFood();
		// }
		// }
		//
		// });
		// cancelButton.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// if (mState == STATE_INSERT) {
		// setResult(RESULT_CANCELED);
		// deleteFood();
		// finish();
		// } else {
		// backupFood();
		// }
		//
		// }
		//
		// });

		Log.e(TAG + ":onCreate", mUri.toString());
		// 获得并保存原始食物信息
		mCursor = getContentResolver().query(mUri, FoodColumn.PROJECTION, null,
				null, null);
		mCursor.moveToFirst();
		originalNameText = mCursor.getString(FoodColumn.NAME_COLUMN);
		originalLocationText = mCursor.getString(FoodColumn.LOCATION_COLUMN);
		originalPriceText = mCursor.getString(FoodColumn.PRICE_COLUMN);
		originalAssessmentText = mCursor
				.getString(FoodColumn.ASSESSMENT_COLUMN);
		originalRecommendationText = mCursor
				.getString(FoodColumn.RECOMMENDATION_COLUMN);
		originalBusinesshoursText = mCursor
				.getString(FoodColumn.BUSINESSHOURS_COLUMN);
		originalRemarksText = mCursor.getString(FoodColumn.REMARKS_COLUMN);

		Log.e(TAG, "end of onCreate()");
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mCursor != null) {
			Log.e(TAG + ":onResume", "count:" + mCursor.getColumnCount());
			// 读取并显示联系人信息
			mCursor.moveToFirst();
			if (mState == STATE_EDIT) {
				setTitle(getText(R.string.food_edit));
			} else if (mState == STATE_INSERT) {
				setTitle(getText(R.string.food_create));
			}
			String name = mCursor.getString(FoodColumn.NAME_COLUMN);
			String location = mCursor.getString(FoodColumn.LOCATION_COLUMN);
			String price = mCursor.getString(FoodColumn.PRICE_COLUMN);
			String assessment = mCursor.getString(FoodColumn.ASSESSMENT_COLUMN);
			String recommendation = mCursor
					.getString(FoodColumn.RECOMMENDATION_COLUMN);
			String businesshours = mCursor
					.getString(FoodColumn.BUSINESSHOURS_COLUMN);
			String remarks = mCursor.getString(FoodColumn.REMARKS_COLUMN);

			nameText.setText(name);
			locationText.setText(location);
			priceText.setText(price);
			assessmentText.setText(assessment);
			recommendationText.setText(recommendation);
			businesshoursText.setText(businesshours);
			remarksText.setText(remarks);

		} else {
			setTitle(getText(R.string.error_msg));
		}

	}

	@Override
	public void onPause() {
		super.onPause();

		if (mCursor != null) {
			String text = nameText.getText().toString();

			if (text.length() == 0) {
				Log.e(TAG + ":onPause", "nameText is null ");
				setResult(RESULT_CANCELED);
				deleteFood();

				// 更新信息
			} else {
				ContentValues values = new ContentValues();
				values.put(FoodColumn.NAME, nameText.getText().toString());
				values.put(FoodColumn.LOCATION, locationText.getText()
						.toString());
				values.put(FoodColumn.PRICE, priceText.getText().toString());
				values.put(FoodColumn.ASSESSMENT, assessmentText.getText()
						.toString());
				values.put(FoodColumn.RECOMMENDATION, recommendationText
						.getText().toString());
				values.put(FoodColumn.BUSINESSHOURS, businesshoursText
						.getText().toString());
				values.put(FoodColumn.REMARKS, remarksText.getText().toString());
				Log.e(TAG + ":onPause", mUri.toString());
				Log.e(TAG + ":onPause", values.toString());
				getContentResolver().update(mUri, values, null, null);
			}
		}
	}

	// 删除食物信息
	private void deleteFood() {
		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
			getContentResolver().delete(mUri, null, null);
			nameText.setText("");
		}

	}

	// 丢弃信息
	private void cancelFood() {
		if (mCursor != null) {
			deleteFood();
		}
		setResult(RESULT_CANCELED);
		finish();

	}

	// 更新 变更的信息
	private void updateFood() {
		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
			ContentValues values = new ContentValues();
			values.put(FoodColumn.NAME, nameText.getText().toString());
			values.put(FoodColumn.LOCATION, locationText.getText().toString());
			values.put(FoodColumn.PRICE, priceText.getText().toString());
			values.put(FoodColumn.ASSESSMENT, assessmentText.getText()
					.toString());
			values.put(FoodColumn.RECOMMENDATION, recommendationText.getText()
					.toString());
			values.put(FoodColumn.BUSINESSHOURS, businesshoursText.getText()
					.toString());
			values.put(FoodColumn.REMARKS, remarksText.getText().toString());
			Log.e(TAG + ":onPause", mUri.toString());
			Log.e(TAG + ":onPause", values.toString());
			getContentResolver().update(mUri, values, null, null);
		}
		setResult(RESULT_CANCELED);
		finish();

	}

	// 取消用，回退到最初的信息
	private void backupFood() {
		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
			ContentValues values = new ContentValues();
			values.put(FoodColumn.NAME, this.originalNameText);
			values.put(FoodColumn.LOCATION, this.originalLocationText);
			values.put(FoodColumn.PRICE, this.originalPriceText);
			values.put(FoodColumn.ASSESSMENT, this.originalAssessmentText);
			values.put(FoodColumn.RECOMMENDATION,
					this.originalRecommendationText);
			values.put(FoodColumn.BUSINESSHOURS, this.originalBusinesshoursText);
			values.put(FoodColumn.REMARKS, this.originalRemarksText);
			Log.e(TAG + ":onPause", mUri.toString());
			Log.e(TAG + ":onPause", values.toString());
			getContentResolver().update(mUri, values, null, null);
		}
		setResult(RESULT_CANCELED);
		finish();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			String text = nameText.getText().toString();
			if (text.length() == 0) {
				setResult(RESULT_CANCELED);
				deleteFood();
				finish();
			} else {
				updateFood();
			}
			return true;
		case R.id.menu_cancel:
			if (mState == STATE_INSERT) {
				setResult(RESULT_CANCELED);
				deleteFood();
				finish();
			} else {
				backupFood();
			}
			return true;
		case R.id.menu_delete: {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(R.string.menu_delete)
					.setMessage("你真的要删除么")
					.setPositiveButton(R.string.menu_delete,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									cancelFood();

								}
							}).setNegativeButton(R.string.menu_cancel, null)
					.create().show();

			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.edit_activity, menu);
		return true;
	}

}
