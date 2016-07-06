package com.example.charts;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.charts.util.DBManager;
import com.example.charts.util.DateUtil;
import com.example.charts.util.ScoreBean;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatisticsFragment extends Fragment implements OnClickListener {

	private View monthView;
	private View weekView;
	private TextView weekTextView;
	private TextView monthTextView;
	private TextView averageTextView;
	private TextView maxTextView;
	private TextView minTextView;
	private View rootView;
	private int id = R.id.statistic_week;
	
	private LineChartView chart;
	private LineChartData weekData;
	private LineChartData monthData;
	private int numberOfLines = 1;
	private int numberOfPoints = 1;
	private int maxNumberOfPoints = 7;
	
	private List<AxisValue> axisValues;
	private List<String> label;
	private List<String> weekLabel;
	private List<String> monthLabel;
	private ArrayList<Float> randomWeek;
	private ArrayList<Float> randomMonth;
	private float[] weekScore = {0f,0f,100f};
	private float[] monthScore = {0f,0f,100f};
	private DBManager mgr;


	private boolean hasAxes = true;
	private boolean hasAxesNames = true;
	private boolean hasLines = true;
	private boolean hasPoints = true;
	private ValueShape shape = ValueShape.CIRCLE;
	private boolean isFilled = false;
	private boolean hasLabels = false;
	private boolean isCubic = false;
	private boolean hasLabelForSelected = false;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_statistics, container,
				false);
		initView();
		return rootView;
	}

	private void initView() {
		mgr = ((MainActivity)getActivity()).getMgr();
		chart = (LineChartView) rootView.findViewById(R.id.chart);
		weekTextView = (TextView) rootView.findViewById(R.id.statistic_week_text);
		monthTextView = (TextView) rootView.findViewById(R.id.statistic_month_text);
		averageTextView = (TextView) rootView.findViewById(R.id.statistic_average);
		maxTextView = (TextView) rootView.findViewById(R.id.statistic_max);
		minTextView = (TextView) rootView.findViewById(R.id.statistic_min);
		weekView = rootView.findViewById(R.id.statistic_week);
		monthView = rootView.findViewById(R.id.statistic_month);
		weekView.setOnClickListener(this);
		monthView.setOnClickListener(this);
		randomWeek = new ArrayList<>();
		randomMonth = new ArrayList<>();
		axisValues = new ArrayList<>();
		weekLabel = new ArrayList<>();
		monthLabel = new ArrayList<>();
		generateValues();
		setStatisticChoice(id);
	}

	public void setScore(float[] score) {
		DecimalFormat decimalFormat=new DecimalFormat(".0");
		String averageString=decimalFormat.format(score[0]);
		averageTextView.setText(averageString);
		String maxScoreString=decimalFormat.format(score[1]);
		maxTextView.setText(maxScoreString);
		String minScoreString=decimalFormat.format(score[2]);
		minTextView.setText(minScoreString);
	}

	private void generateData(LineChartData data,List<Float> randomNumbers,boolean isWeek) {
		List<Line> lines = new ArrayList<Line>();
		
		if (isWeek) {
			if (weekLabel != null) {
				label = weekLabel;
			}
		}else {
			if (monthLabel != null) {
				label = monthLabel;
			}
		}
		
		List<PointValue> values = new ArrayList<PointValue>();
		axisValues.clear();
		for (int j = 0; j < randomNumbers.size(); ++j) {
			values.add(new PointValue(j, randomNumbers.get(j)));
			axisValues.add(new AxisValue(j).setLabel(label.get(j)));
		}

		Line line = new Line(values);
		line.setColor(ChartUtils.COLORS[0]);
		line.setShape(shape);
		line.setCubic(isCubic);
		line.setFilled(isFilled);
		line.setHasLabels(hasLabels);
		line.setHasLabelsOnlyForSelected(hasLabelForSelected);
		line.setHasLines(hasLines);
		line.setHasPoints(hasPoints);
	
		lines.add(line);

		data = new LineChartData(lines);

		if (hasAxes) {
			Axis axisX = new Axis(axisValues);
			Axis axisY = new Axis().setHasLines(true);
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}

		data.setBaseValue(Float.NEGATIVE_INFINITY);
		chart.setLineChartData(data);
	}

	private void generateValues() {
		randomWeek.clear();
		randomMonth.clear();
		weekLabel.clear();
		monthLabel.clear();
		weekScore[0] = 0f;
		weekScore[1] = 0f;
		weekScore[2] =100f;
		monthScore[0] = 0f;
		monthScore[1] = 0f;
		monthScore[2] =100f;
		if (mgr != null) {
			setWeekDatas();
			setMonthDatas();
			
		}else {
			for (int j = 0; j < 7; ++j) {
				float t = (float) Math.random() * 50f + 50;
				randomWeek.add(t);
				if (j == 0) {
					weekScore[0] = t;
				}
				weekScore[0] = (weekScore[0] + t)/2;
				if(weekScore[1] < t){
					weekScore[1] = t;
				}
				if (weekScore[2] > t) {
					weekScore[2] = t;
				}
			}
			weekLabel = new DateUtil().getLastNDays(7,null);
			for (int j = 0; j < 4; ++j) {
				float t = (float) Math.random() * 50f + 50;
				randomMonth.add(t);
//				if (j == 0) {
//					monthScore[0] = t;
//				}
//				monthScore[0] = (monthScore[0] + t)/2;
				if(monthScore[1] < t){
					monthScore[1] = t;
				}
				if (monthScore[2] > t) {
					monthScore[2] = t;
				}
				
			}
			monthLabel.add("0");
			monthLabel.add("3/21~3/27");
			monthLabel.add("3/28~4/3");
			monthLabel.add("4/4~4/10");			
			monthLabel.add("本周");
			randomMonth.add(0,0f);			
		}
		
	
	
		
	}

	public void setMonthDatas() {
		List<ScoreBean> monthScoreBeans = mgr.queryMonthDatas();
		float sum = 0f;
		for (int i = 0; i < monthScoreBeans.size(); i++) {
			float t = monthScoreBeans.get(i).getScore();
			randomMonth.add(0,t);
			sum = sum + t;
			if(monthScore[1] < t){
				monthScore[1] = t;
			}
			if (monthScore[2] > t) {
				monthScore[2] = t;
				
			}
			String dateString = getMonthDateLabel(monthScoreBeans.get(i).getDate());
			monthLabel.add(0,dateString);
		}
		if (monthScoreBeans.size() > 0) {
			monthScore[0] = sum / monthScoreBeans.size();
		}else {
			monthScore[0] = sum;
		}
		if (monthScoreBeans.size() < 4) {
			monthLabel.add(0,"");
			randomMonth.add(0,0f);
		}
		
	}

	private String getMonthDateLabel(String date) {
		String[] dateStrings1 = date.split("~");
		if (dateStrings1.length > 1) {
			String[] dateStrings2 = dateStrings1[0].split("-");
			String dateString1 = dateStrings2[1]+"/"+dateStrings2[2];
			String[] dateStrings3 = dateStrings1[1].split("-");
			String dateString2 = dateStrings3[1]+"/"+dateStrings3[2];
			return dateString1+"~"+dateString2;
		}
		else {
			return getWeekDateLabel(date);
		}
		
	}

	public void setWeekDatas() {
		List<ScoreBean> weekScoreBeans = mgr.queryWeekDatas(); 
		float sum = 0f;
		for (int i = 0; i < weekScoreBeans.size(); i++) {
			float t = weekScoreBeans.get(i).getScore();
			randomWeek.add(0,t);
			sum = sum + t;
			if(weekScore[1] < t){
				weekScore[1] = t;
			}
			if (weekScore[2] > t) {
				weekScore[2] = t;
				
			}
			String dateString = getWeekDateLabel(weekScoreBeans.get(i).getDate());
			weekLabel.add(0,dateString);
		}
		if (weekScoreBeans.size() > 0) {
			weekScore[0] = sum / weekScoreBeans.size();
		}else {
			weekScore[0] = sum;
		}
		
		weekLabel.add(0,"");
		int count = mgr.getCount();
		float average1 = mgr.getAverage();
		float a = average1 * count - weekScore[0] * weekScoreBeans.size();
		int b = count - weekScoreBeans.size();
		if (b > 0) {
			average1 = a / b;
		}else {
			average1 = 0f;
		}
		randomWeek.add(0,average1);
	}
	private String getWeekDateLabel(String date) {
		String[] dateStrings = date.split("-");
		String dateString = dateStrings[1]+"/"+dateStrings[2];
		return dateString;
	}

	private void setStatisticChoice(int id) {
		removeAllChoice();
		switch (id) {
		case R.id.statistic_week:
			weekView.setSelected(true);
			weekTextView.setTextColor(Color.BLACK);
			setScore(weekScore);
			generateData(weekData,randomWeek,true);
			break;
		case R.id.statistic_month:
			monthView.setSelected(true);
			monthTextView.setTextColor(Color.BLACK);
			setScore(monthScore);
			generateData(monthData,randomMonth,false);
			break;
		default:
			break;
		}

	}

	private void removeAllChoice() {
		if(weekView.isSelected()){
			weekView.setSelected(!weekView.isSelected());
			weekTextView.setTextColor(Color.WHITE);
		}
		if(monthView.isSelected()){
			monthView.setSelected(!monthView.isSelected());
			monthTextView.setTextColor(Color.WHITE);
		}
		
	}

	@Override
	public void onClick(View v) {
		id = v.getId();
		setStatisticChoice(id);
	}
	
	public void refreshData() {
		generateValues();
		setStatisticChoice(id);
	}
}
