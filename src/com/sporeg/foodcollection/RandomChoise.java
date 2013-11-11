package com.sporeg.foodcollection;

import java.util.Random;

import com.sporeg.foodcollection.database.FoodColumn;
import com.sporeg.foodcollection.database.FoodProvider;
import com.sporeg.foodcollection.sildemenu.BaseActivity;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RandomChoise extends BaseActivity implements SensorEventListener,
		AnimationListener {

	public RandomChoise() {
		super(R.string.main_randomchoise_label);
		// TODO Auto-generated constructor stub
	}

	private static final String TAG = "RandomChoise";

	// boolean flag = true;
	boolean random_flag = true;
	int max = 0;
	Random numx = new Random();
	int id;
	FoodProvider pro = new FoodProvider();
	Cursor mCursor;
	private Vibrator mVibrator;
	private SensorManager sensorManager;
	private Sensor sensor;
	private Animation out_top_Annotation = null;// �ϰ벿�˳�����
	private Animation out_bottom_Annotation = null;// �°벿�˳�����
	private Animation in_top_Annotation = null;// �ϰ벿���붯��
	private Animation in_bottom_Annotation = null;// //�°벿���붯��
	private int duration = 700;// ����Ч��ʱ���������Լ����ã��˴�Ϊ0.45��
	private ImageView imageView_top = null;
	private ImageView imageView_bottom = null;
	LinearLayout ll = null;

	// private Animation startAnimation;
	// private Animation endAnimation;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.random_choise);
		// endAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		// endAnimation.setFillAfter(true);
		// startAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		// startAnimation.setFillAfter(true);
		mVibrator = (Vibrator) getApplication().getSystemService(
				Service.VIBRATOR_SERVICE);
		mCursor = getContentResolver().query(FoodProvider.CONTENT_URI,
				FoodColumn.PROJECTION, null, null, null);
		LayoutInflater inflater = LayoutInflater.from(this);
		ll = (LinearLayout) inflater.inflate(R.layout.random_choise, null);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_GAME);

		/**
		 * ���ö���Ч��
		 */
		this.out_top_Annotation = new TranslateAnimation(0, 0, -50,
				-(float) getWindowManager().getDefaultDisplay().getHeight() / 2);
		this.out_top_Annotation.setDuration(duration);

		this.out_bottom_Annotation = new TranslateAnimation(0, 0, 0,
				(float) getWindowManager().getDefaultDisplay().getHeight() / 2);
		this.out_bottom_Annotation.setDuration(duration);

		this.in_top_Annotation = new TranslateAnimation(
				0,
				0,
				-(float) getWindowManager().getDefaultDisplay().getHeight() / 2,
				-50);
		this.in_top_Annotation.setDuration(duration);

		this.in_bottom_Annotation = new TranslateAnimation(0, 0,
				(float) getWindowManager().getDefaultDisplay().getHeight() / 2,
				0);
		this.in_bottom_Annotation.setDuration(duration);

		this.out_bottom_Annotation.setAnimationListener(this);
		this.in_bottom_Annotation.setAnimationListener(this);

		/**
		 * �½�����ImageView����չʾҡ��Ч��
		 */
		this.imageView_top = new ImageView(this);
		LinearLayout.LayoutParams params_top = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT, 1);

		this.imageView_top.setLayoutParams(params_top);
		// this.imageView_top.setLayoutParams(new LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		this.imageView_bottom = new ImageView(this);
		LinearLayout.LayoutParams params_bottom = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT, 1);
		this.imageView_bottom.setLayoutParams(params_bottom);
		// this.imageView_bottom.setLayoutParams(new LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		this.imageView_top.setImageResource(R.drawable.bg3);// �����ϰ벿�ֱ���ɫ��ʵ������ͼ����ʾ
		this.imageView_bottom.setImageResource(R.drawable.bg4);// �����°ಿ�ֱ�����ʵ������ͼ����ʾ
		// ʵ��Ӧ�����ϰ벿�ֺ��°벿����һ��ͼ�г������ֵ�
		ll.addView(imageView_top);
		ll.addView(imageView_bottom);
		setContentView(ll);
		// buttonstart = (Button) findViewById(R.id.button_start);
		// buttonstart.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// randomChoise();
		// }
		// // TODO Auto-generated method stub
		//
		// // Uri uri = ContentUris.withAppendedId(getIntent().getData(),
		// // id);
		// // mCursor = managedQuery(uri, FoodColumn.PROJECTION, null,
		// // null, null);
		//
		// // mCursor = pro.query(mUri, FoodColumn.PROJECTION,
		// // FoodColumn._ID, idnum, null);
		// // mCursor.moveToFirst();
		//
		// });

	}

	public void randomChoise() {
		int max = 0;
		int id = 0;

		String random_result = "";

		mCursor.moveToFirst();
		max = mCursor.getCount();
		if (max == 0) {
			random_result = "ľ����ʳT^T";

		} else {
			id = numx.nextInt(max);
			// Log.e(TAG + ":onCreate", "" + id + "  " + max);
			mVibrator.vibrate(new long[] { 0, 50, 50, 50 }, -1);
			random_result = mCursor.getString(FoodColumn.NAME_COLUMN);
			while (id > 0) {
				mCursor.moveToNext();
				id = id - 1;
			}
			random_result = mCursor.getString(FoodColumn.NAME_COLUMN) + "\n"
					+ getString(R.string.price) + "\t"
					+ mCursor.getString(FoodColumn.PRICE_COLUMN);
		}
		Log.e(TAG, "    " + isFinishing());
		if (!isFinishing()) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(R.string.random_result)
					.setMessage(random_result)
					.setPositiveButton(android.R.string.ok,
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									random_flag = true;
								}
							}).create().show();
		}
		;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		float x = event.values[SensorManager.DATA_X];
		float y = event.values[SensorManager.DATA_Y];
		float z = event.values[SensorManager.DATA_Z];
		if (Math.abs(x) >= 17 || Math.abs(y) >= 17 || Math.abs(z) >= 17) {// �жϼ��ٶ�>14ʱ�����ֵ�ǿ����޸ĵġ�
		// if (flag) {
		//
		// flag = false;
		// }
			imageView_bottom.startAnimation(out_bottom_Annotation);// ��ʼ��������ס��startAnimation������setAnimation
			imageView_top.startAnimation(out_top_Annotation);
			if (random_flag) {
				random_flag = false;
				randomChoise();
			}

			sensorManager.unregisterListener(this);// ȡ���������ٸ�Ӧ���������ȡ���Ļ��������⣬ͬѧ�ǿ����Լ�ȥ������ҡ����

		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		if (animation == this.out_bottom_Annotation) {
			imageView_bottom.startAnimation(in_bottom_Annotation);
			imageView_top.startAnimation(in_top_Annotation);
		} else if (animation.equals(this.in_bottom_Annotation)) {
			sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_GAME);// ��ɶ������ٴμ������ٸ�Ӧ��
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

}
