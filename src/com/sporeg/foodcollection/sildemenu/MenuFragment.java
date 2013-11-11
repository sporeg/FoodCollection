package com.sporeg.foodcollection.sildemenu;

import com.actionbarsherlock.app.SherlockFragment;
import com.sporeg.foodcollection.MainActivity;
import com.sporeg.foodcollection.FoodPreferenceActivity;
import com.sporeg.foodcollection.R;
import com.sporeg.foodcollection.RandomChoise;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuFragment extends SherlockFragment implements
		View.OnClickListener {

	@Override
	public void onClick(View paramView) {
		switch (paramView.getId()) {
		case R.id.sidebar_food:
			return;
		case R.id.sidebar_randomsearch:
			startActivity(new Intent(getActivity(), RandomChoise.class));
			return;
		case R.id.sidebar_settings:
			 startActivity(new Intent(getActivity(), FoodPreferenceActivity.class));
			return;
		case R.id.sidebar_exit:
			android.os.Process.killProcess(android.os.Process.myPid());
			return;
		default:
			return;
		}
	}

	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		View localView = paramLayoutInflater.inflate(R.layout.menu,
				paramViewGroup, false);
		localView.findViewById(R.id.sidebar_food).setOnClickListener(this);
		localView.findViewById(R.id.sidebar_randomsearch).setOnClickListener(
				this);
		localView.findViewById(R.id.sidebar_settings).setOnClickListener(this);
		localView.findViewById(R.id.sidebar_exit).setOnClickListener(this);
		return localView;
	}

	public void onResume() {
		super.onResume();
	}

}
