package com.example.charts;

import com.example.charts.util.DBManager;
import com.example.charts.util.ScoreBean;
import com.example.charts.util.SharedPreferencesUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SettingFragment extends Fragment implements OnClickListener {

	private View itemClear;
	private View itemAbout;
	private View rootView;

	private DBManager mgr;
	private SharedPreferencesUtil sp;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_setting, container, false);
		initView();
		return rootView;
	}

	private void initView() {
		itemClear = rootView.findViewById(R.id.setting_clear);
		itemClear.setOnClickListener(this);
		itemAbout = rootView.findViewById(R.id.seting_about);
		itemAbout.setOnClickListener(this);
		sp = new SharedPreferencesUtil(getActivity(), "homeFragment",
				Activity.MODE_PRIVATE);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.setting_clear:
			// Toast.makeText(getActivity(), "update",
			// Toast.LENGTH_SHORT).show();
			// mgr = ((MainActivity)getActivity()).getMgr();
			// mgr.updateTest();
			break;
		case R.id.seting_about:
			Intent intent = new Intent(getContext(), AboutActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}
}
