package com.example.charts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.charts.util.DBManager;
import com.example.charts.util.DateUtil;
import com.example.charts.util.ScoreBean;
import com.example.charts.util.SharedPreferencesUtil;

import android.R.fraction;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment implements OnClickListener {

	private View rootView;
	private View dataButton;
	private View homeView;
	private ViewPager mViewPager;

	private ImageView dataButtonView;
	private TextView tongbuTextView;
	private TextView shujuTextView;

	private ImageView ring;
	// private ImageView rateRing;
	private boolean isRateLocked = true;
	private int[] ringId = { R.id.home_ring_sleep, R.id.home_ring_rate,
			R.id.home_ring_temperature, R.id.home_ring_water,
			R.id.home_ring_light };
	private ImageView sleepIcon;
	private ImageView rateIcon;
	private ImageView tempIcon;
	private ImageView waterIcon;
	private ImageView lightIcon;

	private FragmentPagerAdapter mAdapter;
	private List<Fragment> fragments;
	private SharedPreferencesUtil sp;
	private HashMap<String, Float> sleepDatas = new HashMap<String, Float>();
	private HashMap<String, Float> rateDatas = new HashMap<>();
	private HashMap<String, Float> tempDatas = new HashMap<>();
	private HashMap<String, Float> waterDatas = new HashMap<>();
	private HashMap<String, Float> lightDatas = new HashMap<>();
	private HashMap<String, Float> noiseDatas = new HashMap<>();
	private HashMap<String, Float> shakeDatas = new HashMap<>();
	private boolean hasData = false;
	private boolean isRefresh = false;
	private boolean isLoaded = false;
	private boolean isTest = false;

	public boolean isTest() {
		return isTest;
	}

	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}

	private String curDate;
	private int totalCount;
	private DBManager mgr;

	private Socket socket = null;
	private BufferedReader br = null;
	private StringBuffer sb;

	private Handler handler;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home, container, false);
		mgr = ((MainActivity) getActivity()).getMgr();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Toast.makeText(getActivity(), "保存数据成功", Toast.LENGTH_SHORT)
						.show();
			}
		};
		dataButton = rootView.findViewById(R.id.home_data_btn);
		homeView = rootView.findViewById(R.id.home_view);
		sp = new SharedPreferencesUtil(getActivity(), "homeFragment",
				Activity.MODE_PRIVATE);
		// totalCount = sp.getRecordCount();
		String date = sp.getData("date");
		curDate = new DateUtil().getDate();
		if (date.equals(curDate)) {
			hasData = !hasData;
			isLoaded = !isLoaded;
		}
		setContentView();

		return rootView;
	}

	public void refreshDatas() {
		// Log.d("fragments", "child fragment count: " +
		// getChildFragmentManager().getFragments().size());
		getChildFragmentManager().getFragments().clear();
		// Log.d("fragments", "child fragment count: " +
		// getChildFragmentManager().getFragments().size());
		hasData = false;
		isRefresh = true;
		isLoaded = false;
		fragments.clear();
		mAdapter.notifyDataSetChanged();
		mViewPager.setAdapter(null);
		dataButton.setVisibility(View.VISIBLE);
		homeView.setVisibility(View.INVISIBLE);
		initButton();
		shujuTextView.setText("ING..");
		dataButtonView.setImageResource(R.drawable.home_data0);
		new MyTask().execute();
	}

	public void setContentView() {
		if (hasData) {
			homeView.setVisibility(View.VISIBLE);
			dataButton.setVisibility(View.INVISIBLE);
			initView();

			initDatas();

			setIconSelected(0);
		} else {
			dataButton.setVisibility(View.VISIBLE);
			homeView.setVisibility(View.INVISIBLE);
			initButton();
		}
	}

	private void initButton() {
		dataButtonView = (ImageView) rootView
				.findViewById(R.id.home_data_btn_view);
		tongbuTextView = (TextView) rootView
				.findViewById(R.id.home_data_tongbu);
		shujuTextView = (TextView) rootView.findViewById(R.id.home_data_shuju);
		dataButton.setOnClickListener(this);
	}

	private void setIconSelected(int position) {
		switch (position) {
		case 0:
			sleepIcon.setSelected(true);
			break;
		case 1:
			rateIcon.setSelected(true);
			break;
		case 2:
			tempIcon.setSelected(true);
			break;
		case 3:
			waterIcon.setSelected(true);
			break;
		case 4:
			lightIcon.setSelected(true);
			break;
		default:
			break;
		}

	}

	private void initDatas() {
		if (isLoaded) {
			sleepDatas = sp.getSleepMap();
			rateDatas = sp.getRateMap();
			tempDatas = sp.getTempMap();
			waterDatas = sp.getWaterMap();
			lightDatas = sp.getLightMap();
			noiseDatas = sp.getNoiseMap();
			shakeDatas = sp.getShakeMap();

		}
		fragments = new ArrayList<>();
		SleepFragment sleepFragment = new SleepFragment(sleepDatas);
		RateFragment rateFragment = new RateFragment(rateDatas);
		TempAndWaterFragment tempFragment = new TempAndWaterFragment(tempDatas,
				waterDatas);
		NoiseAndShakeFragment waterFragment = new NoiseAndShakeFragment(
				noiseDatas, shakeDatas);
		LightFragment lightFragment = new LightFragment(lightDatas);
		fragments.add(sleepFragment);
		fragments.add(rateFragment);
		fragments.add(tempFragment);
		fragments.add(waterFragment);
		fragments.add(lightFragment);

		mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {

			@Override
			public int getCount() {
				return fragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return fragments.get(arg0);
			}
		};
		mViewPager.setAdapter(mAdapter);
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				resetIcon();
				ring.setVisibility(View.INVISIBLE);
				ring = (ImageView) rootView.findViewById(ringId[position]);
				ring.setVisibility(View.VISIBLE);
				setIconSelected(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void resetIcon() {
		if (sleepIcon.isSelected()) {
			sleepIcon.setSelected(!sleepIcon.isSelected());
		}
		if (rateIcon.isSelected()) {
			rateIcon.setSelected(!rateIcon.isSelected());
		}
		if (tempIcon.isSelected()) {
			tempIcon.setSelected(!tempIcon.isSelected());
		}

		if (waterIcon.isSelected()) {
			waterIcon.setSelected(!waterIcon.isSelected());
		}

		if (lightIcon.isSelected()) {
			lightIcon.setSelected(!lightIcon.isSelected());
		}
	}

	private void initView() {
		mViewPager = (ViewPager) rootView.findViewById(R.id.home_viewpager);
		ring = (ImageView) rootView.findViewById(R.id.home_ring_sleep);
		ring.setVisibility(View.VISIBLE);
		sleepIcon = (ImageView) rootView.findViewById(R.id.home_choice_sleep);
		rateIcon = (ImageView) rootView.findViewById(R.id.home_choice_rate);
		tempIcon = (ImageView) rootView
				.findViewById(R.id.home_choice_temperature);
		waterIcon = (ImageView) rootView.findViewById(R.id.home_choice_water);
		lightIcon = (ImageView) rootView.findViewById(R.id.home_choice_light);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.home_data_btn:
			shujuTextView.setText("ING..");
			new MyTask().execute();
			break;
		default:
			break;
		}

	}

	private List<HashMap<String, Float>> generateData() {

		List<HashMap<String, Float>> dataList = new ArrayList<>();
		dataList.add(getSleepDatas());
		// dataList.add(getRateDatas());
		// dataList.add(getTempDatas());
		// dataList.add(getWaterDatas());
		// dataList.add(getLightDatas());
		return dataList;
	}

	private HashMap<String, Float> getShakeDatas(List<String> shakeList) {
		HashMap<String, Float> shakeMap = new HashMap<>();
		int count = shakeList.size() / 10;
		int modCount = shakeList.size() % 10;
		for (int i = 0; i < count; i++) {
			float sumTemp = 0;
			for (int j = i * 10; j < i * 10 + 10; j++) {
				float sampleTemp = Float.parseFloat(shakeList.get(j));
				sumTemp = sumTemp + sampleTemp;
			}
			shakeMap.put("shake" + i, sumTemp / 10);

		}
		if (modCount > 0) {
			float sumTemp = 0;
			for (int i = count * 10; i < count * 10 + modCount; i++) {
				float sampleTemp = Float.parseFloat(shakeList.get(i));
				sumTemp = sumTemp + sampleTemp;
			}
			shakeMap.put("shake" + count, sumTemp / modCount);
			count++;
		}
		sp.saveData("shakeCount", count);
		return shakeMap;
	}

	private HashMap<String, Float> getNoiseDatas(List<String> noiseList) {
		HashMap<String, Float> NoiseMap = new HashMap<>();
		int count = noiseList.size() / 10;
		int modCount = noiseList.size() % 10;
		for (int i = 0; i < count; i++) {
			float sumTemp = 0;
			for (int j = i * 10; j < i * 10 + 10; j++) {
				float sampleTemp = Float.parseFloat(noiseList.get(j));
				sumTemp = sumTemp + sampleTemp;
			}
			NoiseMap.put("noise" + i, sumTemp / 10);

		}
		if (modCount > 0) {
			float sumTemp = 0;
			for (int i = count * 10; i < count * 10 + modCount; i++) {
				float sampleTemp = Float.parseFloat(noiseList.get(i));
				sumTemp = sumTemp + sampleTemp;
			}
			NoiseMap.put("noise" + count, sumTemp / modCount);
			count++;
		}
		sp.saveData("noiseCount", count);
		return NoiseMap;
	}

	private HashMap<String, Float> getLightDatas(List<String> lightList) {
		HashMap<String, Float> lightMap = new HashMap<>();
		int count = lightList.size() / 10;
		int modCount = lightList.size() % 10;
		for (int i = 0; i < count; i++) {
			float sumTemp = 0;
			for (int j = i * 10; j < i * 10 + 10; j++) {
				float sampleTemp = Float.parseFloat(lightList.get(j));
				sumTemp = sumTemp + sampleTemp;
			}
			lightMap.put("light" + i, sumTemp / 10);

		}
		if (modCount > 0) {
			float sumTemp = 0;
			for (int i = count * 10; i < count * 10 + modCount; i++) {
				float sampleTemp = Float.parseFloat(lightList.get(i));
				sumTemp = sumTemp + sampleTemp;
			}
			lightMap.put("light" + count, sumTemp / modCount);
			count++;
		}
		sp.saveData("lightCount", count);
		return lightMap;
	}

	private HashMap<String, Float> getWaterDatas(List<String> waterList) {
		HashMap<String, Float> waterMap = new HashMap<>();
		int count = waterList.size() / 10;
		int modCount = waterList.size() % 10;
		for (int i = 0; i < count; i++) {
			float sumTemp = 0;
			for (int j = i * 10; j < i * 10 + 10; j++) {
				float sampleTemp = Float.parseFloat(waterList.get(j));
				sumTemp = sumTemp + sampleTemp;
			}
			waterMap.put("water" + i, sumTemp / 10);

		}
		if (modCount > 0) {
			float sumTemp = 0;
			for (int i = count * 10; i < count * 10 + modCount; i++) {
				float sampleTemp = Float.parseFloat(waterList.get(i));
				sumTemp = sumTemp + sampleTemp;
			}
			waterMap.put("water" + count, sumTemp / modCount);
			count++;
		}
		sp.saveData("waterCount", count);
		return waterMap;
	}

	private HashMap<String, Float> getTempDatas(List<String> tempList) {
		HashMap<String, Float> tempMap = new HashMap<>();
		int count = tempList.size() / 10;
		int modCount = tempList.size() % 10;
		for (int i = 0; i < count; i++) {
			float sumTemp = 0;
			for (int j = i * 10; j < i * 10 + 10; j++) {
				float sampleTemp = Float.parseFloat(tempList.get(j));
				sumTemp = sumTemp + sampleTemp;
			}
			tempMap.put("temp" + i, sumTemp / 10);
			// sp.saveData("temp" + i, randomTemp);
		}
		if (modCount > 0) {
			float sumTemp = 0;
			for (int i = count * 10; i < count * 10 + modCount; i++) {
				float sampleTemp = Float.parseFloat(tempList.get(i));
				sumTemp = sumTemp + sampleTemp;
			}
			tempMap.put("temp" + count, sumTemp / modCount);
			count++;
		}
		sp.saveData("tempCount", count);
		return tempMap;
	}

	private HashMap<String, Float> getRateDatas(List<String> rateList) {
		HashMap<String, Float> rateMap = new HashMap<>();
		for (int i = 0; i < rateList.size(); i++) {
			float randomRate = Float.parseFloat(rateList.get(i));
			rateMap.put("rate" + i, randomRate);
			// sp.saveData("rate" + i, randomRate);
		}
		sp.saveData("rateCount", rateList.size());
		return rateMap;
	}

	public HashMap<String, Float> getSleepDatas() {
		HashMap<String, Float> sleepMap = new HashMap<>();
		float ds = (float) (Math.random() * 2.0f);
		float ss = (float) (Math.random() * (7 - ds));
		float aw = (float) Math.random();
		float sum = ds + ss + aw;
		sleepMap.put("sleep" + 0, ds);
		sp.saveData("sleep" + 0, ds);

		sleepMap.put("sleep" + 1, ss);
		sp.saveData("sleep" + 1, ss);

		sleepMap.put("sleep" + 2, aw);
		sp.saveData("sleep" + 2, aw);

		sleepMap.put("sleep" + 3, sum);
		sp.saveData("sleep" + 3, sum);

		sleepMap.put("sleep" + 4, (ds + ss * 0.5f) / sum * 100f);
		sp.saveData("sleep" + 4, (ds + ss * 0.5f) / sum * 100f);

		sp.saveData("sleepCount", 5);
		return sleepMap;
	}

	private class MyTask extends
			AsyncTask<Void, Integer, List<HashMap<String, Float>>> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<HashMap<String, Float>> doInBackground(Void... params) {
			List<HashMap<String, Float>> dataList = new ArrayList<>();

			if (isTest) {
				getTCPDatas(dataList);
			} else {

				dataList.add(getRandomRateDatas());
				publishProgress(10);
				dataList.add(getRandomTempDatas());
				publishProgress(30);
				dataList.add(getRandomWaterDatas());
				publishProgress(51);
				dataList.add(getRandomLightDatas());
				dataList.add(getRandomNoiseDatas());
				dataList.add(getRandomShakeDatas());
				publishProgress(100);
			}

			dataList.add(0, getSleepDatas());
			return dataList;
		}

		private HashMap<String, Float> getRandomShakeDatas() {
			HashMap<String, Float> shakeMap = new HashMap<>();
			for (int i = 0; i < 8; i++) {
				float randomNoise = (float) (Math.random());
				shakeMap.put("shake" + i, randomNoise);
			}
			sp.saveData("shakeCount", 8);
			return shakeMap;
		}

		private HashMap<String, Float> getRandomNoiseDatas() {
			HashMap<String, Float> noiseMap = new HashMap<>();
			for (int i = 0; i < 8; i++) {
				float randomNoise = (float) (Math.random() + 1);
				noiseMap.put("noise" + i, randomNoise);
			}
			sp.saveData("noiseCount", 8);
			return noiseMap;
		}

		private HashMap<String, Float> getRandomLightDatas() {
			HashMap<String, Float> lightMap = new HashMap<>();
			for (int i = 0; i < 8; i++) {
				float randomLight = (float) (Math.random() + 2);
				lightMap.put("light" + i, randomLight);
				// sp.saveData("light" + i, randomLight);
			}
			sp.saveData("lightCount", 8);
			return lightMap;
		}

		private HashMap<String, Float> getRandomWaterDatas() {
			HashMap<String, Float> waterMap = new HashMap<>();
			for (int i = 0; i < 8; i++) {
				float randomWater = (float) (Math.random() * 5 + 70);
				waterMap.put("water" + i, randomWater);
				// sp.saveData("water" + i, randomWater);
			}
			sp.saveData("waterCount", 8);
			return waterMap;
		}

		private HashMap<String, Float> getRandomTempDatas() {
			HashMap<String, Float> tempMap = new HashMap<>();
			for (int i = 0; i < 8; i++) {
				float randomTemp = (float) (Math.random() * 5 + 20);
				tempMap.put("temp" + i, randomTemp);
				// sp.saveData("temp" + i, randomTemp);
			}
			sp.saveData("tempCount", 8);
			return tempMap;
		}

		private HashMap<String, Float> getRandomRateDatas() {
			HashMap<String, Float> rateMap = new HashMap<>();
			for (int i = 0; i < 80; i++) {
				float randomRate = (float) (Math.random() * 5 + 60);
				rateMap.put("rate" + i, randomRate);
				// sp.saveData("rate" + i, randomRate);
			}
			sp.saveData("rateCount", 80);
			return rateMap;
		}

		public void getTCPDatas(List<HashMap<String, Float>> dataList) {
			try {
				socket = new Socket("192.168.4.1", 8080);
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				new Thread().sleep(50);
				InputStream is = socket.getInputStream();
				byte[] car = new byte[512];
				int len = 0;
				char end = 'e';
				int totalpkg = 0;
				int t = 0;
				out.println("X");
				if ((len = is.read(car)) != -1) {
					String pkg = new String(car, 0, len);
					totalpkg = Integer.parseInt(pkg);
				}
				if (totalpkg != 0) {
					out.println("B");

					sb = new StringBuffer();

					while ((len = is.read(car)) != -1) {
						// len = is.read(car);
						String info = new String(car, 0, len);
						sb.append(info);
						t++;

						publishProgress(t * 100 / totalpkg);

						if ((end = info.charAt(len - 2)) == '=') {
							break;
						}
					}
				}
				// out.println("U");
				out.close();
				socket.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<String> tempList = new ArrayList<>();
			List<String> lightList = new ArrayList<>();
			List<String> rateList = new ArrayList<>();
			List<String> waterList = new ArrayList<>();
			List<String> noiseList = new ArrayList<>();
			List<String> shakeList = new ArrayList<>();
			String string = new String(sb);
			String[] datas = string.split("E");
			for (int i = 0; i < datas.length - 1; i++) {
				String[] frame = datas[i].split("-");
				tempList.add(frame[0]);
				lightList.add(frame[1]);
				rateList.add(frame[2]);
				waterList.add(frame[3]);
				noiseList.add(frame[4]);
				shakeList.add(frame[5]);
			}
			dataList.add(getRateDatas(rateList));
//			publishProgress(40);
			dataList.add(getTempDatas(tempList));
//			publishProgress(60);
			dataList.add(getWaterDatas(waterList));
//			publishProgress(80);
			dataList.add(getLightDatas(lightList));
			dataList.add(getNoiseDatas(noiseList));
			dataList.add(getShakeDatas(shakeList));
//			publishProgress(100);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int cur = values[0];
			if (cur < 25) {
				dataButtonView.setImageResource(R.drawable.home_data0);
			} else if (cur < 50) {
				dataButtonView.setImageResource(R.drawable.home_data1_4);
			} else if (cur < 75) {
				dataButtonView.setImageResource(R.drawable.home_data1_2);
			} else if (cur < 100) {
				dataButtonView.setImageResource(R.drawable.home_data3_4);
			} else {
				dataButtonView.setImageResource(R.drawable.home_data);
			}
			System.out.println(values[0]);
		}

		@Override
		protected void onPostExecute(List<HashMap<String, Float>> result) {

			sleepDatas = result.get(0);
			if (mgr != null) {
				float curScore = sleepDatas.get("sleep" + 4);
				ScoreBean scoreBean = new ScoreBean(curDate,curScore);
				if (isRefresh) {
					mgr.update(scoreBean);
				} else {
					mgr.add(scoreBean);
				}

			}
			rateDatas = result.get(1);
			tempDatas = result.get(2);
			waterDatas = result.get(3);
			lightDatas = result.get(4);
			noiseDatas = result.get(5);
			shakeDatas = result.get(6);
			Toast.makeText(getActivity(), "保存数据中，请勿关闭", Toast.LENGTH_SHORT)
					.show();
			new MySaveThread().start();
			hasData = true;
			isRefresh = true;
			sp.saveData("date", curDate);
			setContentView();
		}

	}

	private class MySaveThread extends Thread {
		@Override
		public void run() {
			for (int i = 0; i < rateDatas.size(); i++) {
				sp.saveData("rate" + i, rateDatas.get("rate" + i));
			}
			for (int i = 0; i < tempDatas.size(); i++) {
				sp.saveData("temp" + i, tempDatas.get("temp" + i));
			}
			for (int i = 0; i < waterDatas.size(); i++) {
				sp.saveData("water" + i, waterDatas.get("water" + i));
			}
			for (int i = 0; i < lightDatas.size(); i++) {
				sp.saveData("light" + i, lightDatas.get("light" + i));
			}
			for (int i = 0; i < noiseDatas.size(); i++) {
				sp.saveData("noise" + i, noiseDatas.get("noise" + i));
			}
			for (int i = 0; i < shakeDatas.size(); i++) {
				sp.saveData("shake" + i, shakeDatas.get("shake" + i));
			}
			handler.sendEmptyMessage(0);
		}
	}
}
