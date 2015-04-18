package com.david.pda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.david.pda.util.io.LocalUtil;
import com.david.pda.util.net.OnLoadImageListener;
import com.david.pda.util.net.SyncDownload;
import com.david.pda.util.other.Bind;
import com.david.pda.util.other.DateUtil;
import com.david.pda.weather.model.Index;
import com.david.pda.weather.model.WeatherData;
import com.david.pda.weather.model.WeatherResult;
import com.david.pda.weather.model.util.L;
import com.david.pda.weather.model.util.WeatherResultUtil;

public class SomeToolsWeatherActivity extends Activity {
	public final static String DEFAULT_CITY = "DEFAULT_CITY";
	public final static String DEFAULT_WEATHER = "DEFAULT_WEATHER";
	GridView indexGridView;
	WeatherResult weatherResult;
	TextView cityText;
	TextView pmText;
	TextView dateText;
	TextView windText;
	TextView temperatureText;
	TextView weatherText;
	ImageView weatherPicView;
	ImageButton backwardImageButton;
	ImageButton searchButton;
	AutoCompleteTextView searchAutoText;
	ImageView refreshButton;
	private String defaultCity = "上海";
	private String defaultWeather = "";
	SharedPreferences sp;
	TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.main_some_tools_weather_grid);
		// 详见StrictMode文档
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // or
				.penaltyLog().build());
		indexGridView = (GridView) findViewById(R.id.main_some_tools_weather_indexgrid);
		cityText = (TextView) findViewById(R.id.main_some_tools_weather_city);
		pmText = (TextView) findViewById(R.id.main_some_tools_weather_pm);
		dateText = (TextView) findViewById(R.id.main_some_tools_weather_date);
		windText = (TextView) findViewById(R.id.main_some_tools_weather_wind);
		temperatureText = (TextView) findViewById(R.id.main_some_tools_weather_temperature);
		weatherText = (TextView) findViewById(R.id.main_some_tools_weather_weather);
		weatherPicView = (ImageView) findViewById(R.id.main_some_tools_weather_weatherPic);
		backwardImageButton = (ImageButton) findViewById(R.id.main_some_tools_weather_topbar_backward);
		searchButton = (ImageButton) findViewById(R.id.main_some_tools_weather_topbar_search_btn);
		searchAutoText = (AutoCompleteTextView) findViewById(R.id.main_some_tools_weather_topbar_search_text);
		refreshButton = (ImageView) findViewById(R.id.main_some_tools_weather_topbar_refresh_btn);
		title = (TextView) findViewById(R.id.main_some_tools_weather_topbar_title);
		Bind.bindReturn(backwardImageButton, this, MainActivity.class,
				MainActivity.POSTION_SOME_TOOLS);
		searchAutoText.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, getCitys()));
		searchButton.setOnClickListener(new SearchButtomOnClickListener());
		cityText.setOnClickListener(new CityTextOnclickListener());
		refreshButton.setOnClickListener(new RefreshButtonOnClickListener());
		// getWeather from remote
		// 显示刷新
		// 查看文件，如果有显示；如果没有，调用虾米那的getWeatherHandler.post(getWeatherTread);
		Log.i(L.t, this.getClass().getName());
		sp = getSharedPreferences(this.getClass().getName(), MODE_MULTI_PROCESS);
		defaultCity = sp.getString(DEFAULT_CITY, null);
		defaultWeather = sp.getString(DEFAULT_WEATHER, null);
		if (defaultCity == null) {
			defaultCity = "北京";
			Toast.makeText(SomeToolsWeatherActivity.this,
					"您还没有设置城市，将默认" + defaultCity, Toast.LENGTH_SHORT).show();
			sp.edit().putString(DEFAULT_CITY, defaultCity).commit();
			getWeatherHandler.post(getWeatherTread);
		} else {
			if (defaultWeather == null) {
				getWeatherHandler.post(getWeatherTread);
			} else {
				// 从文件读取
				weatherResult = WeatherResultUtil.convertBean(defaultWeather);
				showWeatherToView();
				if (!DateUtil.formatYYYY_MM_DD(System.currentTimeMillis())
						.equals(weatherResult.getDate())) {
					Toast.makeText(this, "数据已经过时，请更新！", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}

	// get citys from assets
	private String[] getCitys() {
		String[] citys = null;
		String info = LocalUtil.getAssertFileToString(this, "citys.txt");
		String[] line = info.split("\n");
		if (line != null && line.length > 0) {
			citys = new String[line.length * 2];
			String[] pline = null;
			for (int i = 0, k = 0; i < line.length; i++) {
				pline = line[i].split("\t");
				if (pline.length > 1) {
					citys[k++] = pline[0];
					citys[k++] = pline[1];
				}
			}
		}
		return citys;
	}

	HandlerThread getWeatherTread = new HandlerThread("getWeather") {
		@Override
		public void run() {
			Log.i(L.t, "run start");
			Log.i(L.t, "get city from remote");
			defaultWeather = WeatherResultUtil.getWeatherStr(defaultCity);
			Message m = getWeatherHandler.obtainMessage();
			if (defaultWeather == null) {
				m.arg1 = 0;
			} else {
				m.arg1 = 1;
			}
			getWeatherHandler.sendMessage(m);
			getWeatherHandler.removeCallbacks(getWeatherTread);
			refreshButton.clearAnimation();
		}

	};

	@SuppressLint("HandlerLeak")
	Handler getWeatherHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == 0) {
				Toast.makeText(SomeToolsWeatherActivity.this, "请确认网络是否已经连接！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			sp.edit().putString(DEFAULT_WEATHER, defaultWeather).commit();
			sp.edit().putString(DEFAULT_CITY, defaultCity).commit();
			weatherResult = WeatherResultUtil.convertBean(defaultWeather);
			showWeatherToView();
			Toast.makeText(SomeToolsWeatherActivity.this, "刷新成功！",
					Toast.LENGTH_SHORT).show();
		}
	};

	public void showWeatherToView() {
		cityText.setText(weatherResult.getWeather()[0].getCity());
		pmText.setText("pm:" + weatherResult.getWeather()[0].getPm25());
		WeatherData wd = weatherResult.getWeather()[0].getWeather_data()[0];
		dateText.setText(wd.getDate());
		dateText.setOnClickListener(new DateTextOnClickListener());
		windText.setText(wd.getWind());
		temperatureText.setText(wd.getTemperature());
		weatherText.setText(wd.getWeather());
		String url = null;
		if (DateUtil.isDay()) {
			url = weatherResult.getWeather()[0].getWeather_data()[0]
					.getDayPictureUrl();
		} else {
			url = weatherResult.getWeather()[0].getWeather_data()[0]
					.getNightPictureUrl();
		}
		SyncDownload.onLoadImage(url, new OnLoadImageListener() {
			@Override
			public void OnLoadImage(Bitmap bitmap, String bitmapPath) {
				weatherPicView.setImageBitmap(bitmap);
			}
		});
		List<Map<String, Object>> arryList = new ArrayList<Map<String, Object>>();
		Index[] indexs = weatherResult.getWeather()[0].getIndex();
		Map<String, Object> item = null;
		for (int i = 0; i < indexs.length; i++) {
			item = new HashMap<String, Object>();
			item.put("image", R.drawable._main_some_tools_weather);
			item.put("text", indexs[i].getTitle() + ":" + indexs[i].getZs());
			arryList.add(item);
			Log.i(L.t, item.get("image") + ";" + item.get("text"));
		}
		SimpleAdapter imageItems = new SimpleAdapter(this, arryList,
				R.layout.grid_item, new String[] { "image", "text" },
				new int[] { R.id.grid_item_image, R.id.grid_item_text });
		indexGridView.setAdapter(imageItems);// 添加适配器
		indexGridView
				.setOnItemClickListener(new IndexViewOnItemClickListener());
	}

	class SearchButtomOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			String cityName = searchAutoText.getText().toString();
			if (cityName == "" || cityName == null) {
				Toast.makeText(SomeToolsWeatherActivity.this, "找不到对应的城市",
						Toast.LENGTH_SHORT).show();
				return;
			}
			String cityCN = LocalUtil.getCity(SomeToolsWeatherActivity.this,
					cityName);
			if (cityCN == null) {
				Toast.makeText(SomeToolsWeatherActivity.this, "找不到对应的城市",
						Toast.LENGTH_SHORT).show();
				return;
			} else {// city is not null
				if (cityCN.equals(defaultCity)) {
					Toast.makeText(SomeToolsWeatherActivity.this,
							cityName + "已经是默认城市", Toast.LENGTH_SHORT).show();
					return;
				} else {
					defaultCity = cityCN;
					Editor e = sp.edit();
					e.putString(DEFAULT_CITY, cityCN);
					e.commit();
					Toast.makeText(SomeToolsWeatherActivity.this,
							"已将默认城市修改为：" + cityName, Toast.LENGTH_SHORT).show();
					getWeatherHandler.post(getWeatherTread);
				}
			}
			// 拿着city找到对应的提交请求内容
		}
	}

	class RefreshButtonOnClickListener implements OnClickListener {
		public void onClick(View view) {
			AnimationSet as = new AnimationSet(true);
			RotateAnimation ra = new RotateAnimation(0, 360,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			ra.setDuration(1000);
			as.addAnimation(ra);
			refreshButton.startAnimation(as);
			getWeatherHandler.post(getWeatherTread);
		}
	}

	class CityTextOnclickListener implements OnClickListener {
		@SuppressLint("ShowToast")
		@Override
		public void onClick(View view) {
			Log.i(L.t, "dateTextView is clicked");
			if (searchAutoText.getVisibility() == View.INVISIBLE) {
				searchAutoText.setVisibility(View.VISIBLE);
				searchButton.setVisibility(View.VISIBLE);
				refreshButton.setVisibility(View.INVISIBLE);
				searchAutoText.setFocusable(true);
				searchAutoText.requestFocus();
				title.setVisibility(View.INVISIBLE);
			} else {
				searchAutoText.setVisibility(View.INVISIBLE);
				searchButton.setVisibility(View.INVISIBLE);
				refreshButton.setVisibility(View.VISIBLE);
				title.setVisibility(View.VISIBLE);
			}
		}
	}

	class DateTextOnClickListener implements OnClickListener {
		@SuppressLint("ShowToast")
		@Override
		public void onClick(View view) {
			Log.i(L.t, "dateTextView is clicked");
			WeatherData[] data = weatherResult.getWeather()[0]
					.getWeather_data();
			Toast.makeText(SomeToolsWeatherActivity.this, data[0].getDate(),
					Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(SomeToolsWeatherActivity.this,
					SomeToolsWeatherDataActivity.class);
			intent.putExtra("weather_data", data);
			startActivity(intent);
		}

	}

	class IndexViewOnItemClickListener implements OnItemClickListener {

		@SuppressLint("ShowToast")
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Log.i(L.t, position + "");
			Index[] indexs = weatherResult.getWeather()[0].getIndex();
			Index index = indexs[position];
			Toast.makeText(SomeToolsWeatherActivity.this, index.getDes(),
					Toast.LENGTH_SHORT).show();
		}

	}
}
