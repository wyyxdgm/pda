package com.david.pda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TestBarActivity extends ListActivity {

	private static final int SERIES_NR = 2;
	/** Called when the activity is first created. */
	private ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);
		// 加入 ListItem “ 调度查询 ”
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", " 柱状图 ");
		map.put("desc", " 显示柱状图 ");
		maps.add(map);
		// 构建 listView 的适配器
		SimpleAdapter adapter = new SimpleAdapter(this, maps,
				android.R.layout.simple_list_item_2, // SDK 库中提供的一个包含两个 TextView
														// 的 layout
				new String[] { "name", "desc" }, // maps 中的两个 key
				new int[] { android.R.id.text1, android.R.id.text2 } // 两个
																		// TextView
																		// 的 id
		);
		this.setListAdapter(adapter);

	}

	// ListItem 监听器方法
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		XYMultipleSeriesRenderer renderer = getBarDemoRenderer();
		Intent intent = ChartFactory.getBarChartIntent(this,
				getBarDemoDataset(), renderer, Type.DEFAULT);
		startActivity(intent);
	}

	private XYMultipleSeriesDataset getBarDemoDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		final int nr = 10;
		Random r = new Random();
		for (int i = 0; i < SERIES_NR; i++) {
			CategorySeries series = new CategorySeries("Demo series " + (i + 1));
			for (int k = 0; k < nr; k++) {
				series.add(100 + r.nextInt() % 100);
			}
			dataset.addSeries(series.toXYSeries());
		}
		return dataset;
	}

	public XYMultipleSeriesRenderer getBarDemoRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.BLUE);
		renderer.addSeriesRenderer(r);
		r = new SimpleSeriesRenderer();
		r.setColor(Color.GREEN);
		renderer.addSeriesRenderer(r);
		setChartSettings(renderer);
		return renderer;
	}

	private void setChartSettings(XYMultipleSeriesRenderer renderer) {
		renderer.setChartTitle("Chart demo");
		renderer.setXTitle("x values");
		renderer.setYTitle("y values");
		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(10.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(210);
	}
}