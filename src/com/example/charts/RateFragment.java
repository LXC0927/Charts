package com.example.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class RateFragment extends Fragment implements OnClickListener {

	private View rootView;

	private LineChartView chart;
	private PreviewLineChartView previewChart;
	private LineChartData data;

	private boolean isCubic = true;

	private LineChartData previewData;
	private HashMap<String, Float> rateDatas;

	public RateFragment(HashMap<String, Float> rateDatas) {
		this.rateDatas = rateDatas;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home_rate, container,
				false);
		initView();
		return rootView;
	}

	private void initView() {
		chart = (LineChartView) rootView.findViewById(R.id.chart);
		previewChart = (PreviewLineChartView) rootView
				.findViewById(R.id.chart_preview);

		generateData();
		
		chart.setLineChartData(data);
		// Disable zoom/scroll for previewed chart, visible chart ranges depends
		// on preview chart viewport so
		// zoom/scroll is unnecessary.
		chart.setZoomEnabled(false);
		chart.setScrollEnabled(false);

		previewChart.setLineChartData(previewData);
		previewChart.setContainerScrollEnabled(true,
				ContainerScrollType.HORIZONTAL);
		previewChart.setViewportChangeListener(new ViewportListener());

		previewX(false);
	}

	private void previewX(boolean animate) {
		Viewport tempViewport = new Viewport(chart.getMaximumViewport());		
		float dx = tempViewport.width() / 3;
		tempViewport.inset(dx, -5);
		
		
		
		if (animate) {
			previewChart.setCurrentViewportWithAnimation(tempViewport);
		} else {
			previewChart.setCurrentViewport(tempViewport);
		}
		previewChart.setZoomType(ZoomType.HORIZONTAL);
	}

	private void generateData() {
		int numValues = rateDatas.size();

		List<PointValue> values = new ArrayList<PointValue>();
		for (int i = 0; i < numValues; ++i) {
			values.add(new PointValue(i, rateDatas.get("rate" + i)));
		}

		Line line = new Line(values);
		line.setColor(ChartUtils.COLOR_BLUE);
		line.setHasPoints(false);// too many values so don't draw points.
		line.setCubic(isCubic);

		
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);

		data = new LineChartData(lines);
		data.setAxisXBottom(new Axis());
		data.setAxisYLeft(new Axis().setHasLines(true));

		
		// prepare preview data, is better to use separate deep copy for preview
		// chart.
		// Set color to grey to make preview area more visible.
		previewData = new LineChartData(data);
		previewData.getLines().get(0).setColor(ChartUtils.DEFAULT_DARKEN_COLOR);

	}

	private class ViewportListener implements ViewportChangeListener {

		@Override
		public void onViewportChanged(Viewport newViewport) {
			// don't use animation, it is unnecessary when using preview chart.
			chart.setCurrentViewport(newViewport);
		}

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

	}
}
