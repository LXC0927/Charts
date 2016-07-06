package com.example.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class LightFragment extends Fragment implements OnClickListener {

	private View rootView;

	private LineChartView chart;
	private LineChartData data;
	private int numberOfLines = 1;
	private boolean hasAxes = true;
	private boolean hasAxesNames = true;
	private boolean hasLines = true;
	private boolean hasPoints = true;
	private ValueShape shape = ValueShape.CIRCLE;
	private boolean isFilled = true;
	private boolean hasLabels = false;
	private boolean isCubic = false;
	private boolean hasLabelForSelected = false;
	private boolean pointsHaveDifferentColor;

	private HashMap<String, Float> lightDatas;

	public LightFragment(HashMap<String, Float> lightDatas) {
		this.lightDatas = lightDatas;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home_light, container,
				false);
		initView();
		return rootView;
	}

	private void initView() {
		chart = (LineChartView) rootView.findViewById(R.id.chart);
		generateData();
	}

	private void generateData() {
		List<Line> lines = new ArrayList<Line>();

		List<PointValue> values = new ArrayList<PointValue>();
		for (int j = 0; j < lightDatas.size(); ++j) {
			values.add(new PointValue(j, lightDatas.get("light" + j)));
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
		if (pointsHaveDifferentColor) {
			line.setPointColor(ChartUtils.COLORS[(0 + 1)
					% ChartUtils.COLORS.length]);
		}
		lines.add(line);

		data = new LineChartData(lines);

		if (hasAxes) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);
//			if (hasAxesNames) {
//				axisX.setName("Axis X");
//				axisY.setName("Axis Y");
//			}
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}

		data.setBaseValue(Float.NEGATIVE_INFINITY);
		chart.setLineChartData(data);
		resetViewport();

	}

	private void resetViewport() {
		final Viewport v = new Viewport(chart.getMaximumViewport());
		v.bottom = 0;
//		v.top = 10;
		v.left = 0;
		v.right = lightDatas.size()-1;
		chart.setMaximumViewport(v);
		chart.setCurrentViewport(v);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

	}
}
