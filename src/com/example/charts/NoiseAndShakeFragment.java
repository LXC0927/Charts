package com.example.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class NoiseAndShakeFragment extends Fragment implements OnClickListener {

	private View rootView;

	private LineChartView chart;
	private LineChartData data;
	private int numberOfLines = 1;
	private boolean hasAxes = true;
	private boolean hasAxesNames = true;
	private boolean hasLines = true;
	private boolean hasPoints = true;
	private ValueShape shape = ValueShape.CIRCLE;
	private boolean isFilled = false;
	private boolean hasLabels = false;
	private boolean isCubic = false;
	private boolean hasLabelForSelected = false;
	private boolean pointsHaveDifferentColor;

	private HashMap<String, Float> noiseDatas;
	private HashMap<String, Float> shakeDatas;

	public NoiseAndShakeFragment(HashMap<String, Float> noiseDatas,
			HashMap<String, Float> shakeDatas) {
		this.noiseDatas = noiseDatas;
		this.shakeDatas = shakeDatas;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home_noiseandshake,
				container, false);
		initView();
		return rootView;
	}

	private void initView() {
		chart = (LineChartView) rootView.findViewById(R.id.chart);
		generateData();
	}

	private void generateData() {
		List<Line> lines = new ArrayList<Line>();

		lines.add(getNoiseLine());
		lines.add(getShakeLine());
		data = new LineChartData(lines);

		if (hasAxes) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);
			// if (hasAxesNames) {
			// axisX.setName("Axis X");
			// axisY.setName("Axis Y");
			// }
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

	private Line getShakeLine() {

		List<PointValue> values = new ArrayList<PointValue>();
		for (int j = 0; j < shakeDatas.size(); ++j) {
			values.add(new PointValue(j, shakeDatas.get("shake" + j)));
		}

		Line line = new Line(values);
		line.setColor(ChartUtils.COLOR_BLUE);
		line.setShape(shape);
		line.setCubic(isCubic);
		line.setFilled(isFilled);
		line.setHasLabels(hasLabels);
		line.setHasLabelsOnlyForSelected(hasLabelForSelected);
		line.setHasLines(hasLines);
		line.setHasPoints(hasPoints);
		return line;
	}

	private Line getNoiseLine() {
		List<PointValue> values = new ArrayList<PointValue>();
		for (int j = 0; j < noiseDatas.size(); ++j) {
			values.add(new PointValue(j, noiseDatas.get("noise" + j)));
		}

		Line line = new Line(values);
		line.setColor(ChartUtils.COLOR_ORANGE);
		line.setShape(shape);
		line.setCubic(isCubic);
		line.setFilled(isFilled);
		line.setHasLabels(hasLabels);
		line.setHasLabelsOnlyForSelected(hasLabelForSelected);
		line.setHasLines(hasLines);
		line.setHasPoints(hasPoints);
		return line;
	}

	private void resetViewport() {
		final Viewport v = new Viewport(chart.getMaximumViewport());
		v.bottom = 0;
		// v.top = 10;
		v.left = 0;
		v.right = noiseDatas.size() - 1;
		chart.setMaximumViewport(v);
		chart.setCurrentViewport(v);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

	}
}
