package com.example.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class TempAndWaterFragment extends Fragment implements OnClickListener {

	private View rootView;

	private static final int DEFAULT_DATA = 0;
	private static final int SUBCOLUMNS_DATA = 1;
	private static final int STACKED_DATA = 2;
	private static final int NEGATIVE_SUBCOLUMNS_DATA = 3;
	private static final int NEGATIVE_STACKED_DATA = 4;

	private ComboLineColumnChartView chart;
	private ComboLineColumnChartData data;
	private boolean hasLines = true;
	private boolean hasPoints = true;
	private boolean hasAxes = true;
	private boolean hasAxesNames = true;
	private boolean hasLabels = false;
	private boolean hasLabelForSelected = false;
	private int dataType = DEFAULT_DATA;

	private HashMap<String, Float> tempDatas;
	private HashMap<String, Float> waterDatas;

	public TempAndWaterFragment(HashMap<String, Float> tempDatas,
			HashMap<String, Float> waterDatas) {
		this.tempDatas = tempDatas;
		this.waterDatas = waterDatas;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home_tempandwater, container,
				false);
		initView();
		return rootView;
	}

	private void initView() {
		chart = (ComboLineColumnChartView) rootView.findViewById(R.id.chart);

		generateDefaultData();
	}

	private void generateDefaultData() {

		data = new ComboLineColumnChartData(generateColumnData(),
				generateLineData());

		if (hasAxes) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}

		chart.setZoomType(ZoomType.HORIZONTAL);
		chart.setComboLineColumnChartData(data);
		resetViewport();
	}

	private LineChartData generateLineData() {
		List<Line> lines = new ArrayList<Line>();

		List<PointValue> values = new ArrayList<PointValue>();
		for (int j = 0; j < tempDatas.size(); ++j) {
			values.add(new PointValue(j, tempDatas.get("temp"+j)));
		}

		Line line = new Line(values);
		line.setColor(ChartUtils.COLOR_ORANGE);
		line.setHasLabels(hasLabels);
		line.setHasLines(hasLines);
		line.setHasPoints(hasPoints);
		lines.add(line);

		LineChartData lineChartData = new LineChartData(lines);

		return lineChartData;
	}

	private ColumnChartData generateColumnData() {
		int numSubcolumns = 1;
		List<Column> columns = new ArrayList<Column>();
		List<SubcolumnValue> values;
		for (int i = 0; i < waterDatas.size(); ++i) {

			values = new ArrayList<SubcolumnValue>();
			for (int j = 0; j < numSubcolumns; ++j) {
				values.add(new SubcolumnValue(waterDatas.get("water" + i),
						ChartUtils.COLOR_BLUE));
			}

			Column column = new Column(values);
			columns.add(column);
		}

		ColumnChartData columnChartData = new ColumnChartData(columns);
		return columnChartData;
	}

	private void resetViewport() {
		final Viewport v = new Viewport(chart.getMaximumViewport());
		v.bottom = 0;
//		v.top = 40;
		v.left = 0;
		v.right = tempDatas.size() - 1;
		chart.setMaximumViewport(v);
		chart.setCurrentViewport(v);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

	}
}
