package com.example.charts;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.charts.util.DateUtil;

import lecho.lib.hellocharts.formatter.SimplePieChartValueFormatter;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SleepFragment extends Fragment implements OnClickListener{
	
	private Context context;
	
	private View rootView;
	private TextView dateTextView;
	private ImageView refresh;
	private TextView testTextView;
	private boolean isTest = false;
	private TextView testTime;
	private TextView commentWords;
	private String[] words;
	
	
	private PieChartView chart;
	private PieChartData data;
	private HashMap<String, Float> sleepDatas;

	private boolean hasLabels = false;
	private boolean hasLabelsOutside = false;
	private boolean hasCenterCircle = true;
	private boolean hasCenterText1 = true;
	private boolean hasCenterText2 = false;
	private boolean isExploded = false;
	private boolean hasLabelForSelected = true;
	
	private int[] sleepColor = {Color.parseColor("#405fdc"),Color.parseColor("#62e5f8"),Color.parseColor("#e7ccb8")};
	private String time = "005";
	
	public SleepFragment(HashMap<String, Float> sleepDatas) {
		this.sleepDatas = sleepDatas;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home_sleep, container, false);
		initView();
		return rootView;
	}

	private void initView() {
		chart = (PieChartView) rootView.findViewById(R.id.chart);
		dateTextView = (TextView) rootView.findViewById(R.id.home_sleep_date);
		dateTextView.setText(new DateUtil().getCurrentDate());
		testTextView = (TextView) rootView.findViewById(R.id.home_sleep_test);
		testTextView.setOnClickListener(this);
		
		commentWords = (TextView) rootView.findViewById(R.id.sleep_comment_words);
		words = getActivity().getResources().getStringArray(R.array.sleep_comment);
		
		testTime = (TextView) rootView.findViewById(R.id.home_sleep_testtime);
		testTime.setOnClickListener(this);
		refresh = (ImageView) rootView.findViewById(R.id.home_sleep_refresh);
		refresh.setOnClickListener(this);
		generateData();
	}

	private void generateData() {
		int numValues = 3;

        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < numValues; ++i) {
            SliceValue sliceValue = new SliceValue(sleepDatas.get("sleep"+i),sleepColor[i]);
            values.add(sliceValue);
        }

        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);
        data.setFormatter(new SimplePieChartValueFormatter(1));

        if (hasCenterText1) {
        	DecimalFormat decimalFormat=new DecimalFormat(".0");
    		String score=decimalFormat.format(sleepDatas.get("sleep"+4));
            data.setCenterText1(score+"分");
            float sleepScore = Float.parseFloat(score);
            if (sleepScore > 80) {
				commentWords.setText(words[0]);
			}else if (sleepScore > 60) {
				commentWords.setText(words[1]);
			}else {
				commentWords.setText(words[2]);
			}
            // Get roboto-italic font.
            
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
            data.setCenterText1Color(Color.WHITE);
            data.setCenterText1Typeface(tf);

            // Get font size from dimens.xml and convert it to sp(library uses sp values).
            data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
        }

//        chart.setValueSelectionEnabled(hasLabelForSelected);
        chart.setPieChartData(data);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.home_sleep_refresh:
			((HomeFragment)getParentFragment()).refreshDatas();
			break;

		case R.id.home_sleep_test:
//			new MyTestThread().start();
			isTest = !((HomeFragment)getParentFragment()).isTest();
			if (isTest) {
				((HomeFragment)getParentFragment()).setTest(true);
				Toast.makeText(getActivity(), "测试模式", Toast.LENGTH_SHORT).show();
			}else {
				((HomeFragment)getParentFragment()).setTest(false);
				Toast.makeText(getActivity(), "Random模式", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.home_sleep_testtime:
			if (testTime.getText().toString().equals("10S")) {
				time = "005";
				testTime.setText("5S");
			}else {
				time = "010";
				testTime.setText("10S");
			}
			new MyTestThread().start();
			Toast.makeText(getActivity(), "发送测试时间成功", Toast.LENGTH_SHORT).show();
		default:
			break;
		}
		
	}
	
	class MyTestThread extends Thread{
		@Override
		public void run() {
			try {
				Socket socket = new Socket("192.168.4.1", 8080);
				PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);         
                new Thread().sleep(50);
				out.println("M"+time);
				out.close();
				socket.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}
