package com.david.pda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import com.david.pda.adapter.WeatherDataListAdapter;
import com.david.pda.application.SysApplication;
import com.david.pda.util.other.Bind;
import com.david.pda.weather.model.WeatherData;

public class SomeToolsWeatherDataActivity extends Activity {
	ListView dataListView;
	WeatherData[] data;
	ImageButton backwardImageButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_some_tools_weather_data);
		SysApplication.getInstance().addActivity(this);
		loadData();
		dataListView = (ListView) findViewById(R.id.main_some_tools_weather_data_list);
		dataListView.setAdapter(new WeatherDataListAdapter(this));
		backwardImageButton = (ImageButton) findViewById(R.id.main_some_tools_weather_topbar_backward);
		Bind.bindReturn(backwardImageButton, this,
				SomeToolsWeatherActivity.class);
	}

	private void loadData() {
		Intent i = getIntent();
		Object[] os = (Object[]) i.getSerializableExtra("weather_data");
		data = new WeatherData[os.length];
		int index = 0;
		for (Object o : os) {
			data[index++] = (WeatherData) o;
		}
	}

	public WeatherData[] getData() {
		return data;
	}
}
