package com.example.charts;

import com.example.charts.util.DBManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener {

	
	private ImageView iconHome;
	private ImageView iconSetting;
	private ImageView iconStatistics;
	private TextView titleTextView;

	private Fragment homeFragment;
	private Fragment statisticsFragment;
	private Fragment settingFragment;
	
	private DBManager mgr;

	public DBManager getMgr() {
		return mgr;
	}

	private int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mgr = new DBManager(this);
		initView();
		selectFragment(position);
		setIconSelected(position);
	}

	@Override
	protected void onDestroy() {
		mgr.closeDB();
		super.onDestroy();
	}
	private void selectFragment(int i) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		hideFragment(transaction);
		switch (i) {
		case 0:
			if (homeFragment == null) {
				homeFragment = new HomeFragment();
				transaction.add(R.id.content, homeFragment);
			} else {
				transaction.show(homeFragment);
			}
			break;
		case 1:
			if (statisticsFragment == null) {
				statisticsFragment = new StatisticsFragment();
				transaction.add(R.id.content, statisticsFragment);
			} else {
				transaction.show(statisticsFragment);
				((StatisticsFragment) statisticsFragment).refreshData();
			}
			break;
		case 2:
			if (settingFragment == null) {
				settingFragment = new SettingFragment();
				transaction.add(R.id.content, settingFragment);
			} else {
				transaction.show(settingFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}

	private void initView() {
		titleTextView = (TextView) findViewById(R.id.top_title);
		iconHome = (ImageView) findViewById(R.id.iconhome);
		iconHome.setOnClickListener(this);
		iconStatistics = (ImageView) findViewById(R.id.iconstatistics);
		iconStatistics.setOnClickListener(this);
		iconSetting = (ImageView) findViewById(R.id.iconsetting);
		iconSetting.setOnClickListener(this);
	}

	private void hideFragment(FragmentTransaction transaction) {
		if (homeFragment != null) {
			transaction.hide(homeFragment);
		}
		if (statisticsFragment != null) {
			transaction.hide(statisticsFragment);
		}
		if (settingFragment != null) {
			transaction.hide(settingFragment);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iconhome:
			position = 0;
			selectFragment(position);	
			setIconSelected(position);
			break;

		case R.id.iconstatistics:
			position = 1;
			selectFragment(position);
			setIconSelected(position);
			break;

		case R.id.iconsetting:
			position = 2;
			selectFragment(position);
			setIconSelected(position);
			break;
		default:
			break;
		}

	}

	private void setIconSelected(int position) {
		removeAllSelected();
		switch (position) {
		case 0:
			iconHome.setSelected(true);
			titleTextView.setText("首页");
			break;
		case 1:
			iconStatistics.setSelected(true);
			titleTextView.setText("统计");
			break;
		case 2:
			iconSetting.setSelected(true);
			titleTextView.setText("设置");
			break;
		default:
			break;
		}
	}

	private void removeAllSelected() {
		if (iconHome.isSelected()) {
			iconHome.setSelected(!iconHome.isSelected());
		}
		if (iconStatistics.isSelected()) {
			iconStatistics.setSelected(!iconStatistics.isSelected());
		}
		if (iconSetting.isSelected()) {
			iconSetting.setSelected(!iconSetting.isSelected());
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		position = savedInstanceState.getInt("position");
		selectFragment(position);
		setIconSelected(position);
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("position", position);
		// super.onSaveInstanceState(outState);
	}
}
