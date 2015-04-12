package com.david.pda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.david.pda.util.net.OnLoadImageListener;
import com.david.pda.util.net.SyncDownload;
import com.david.pda.util.other.Bind;
import com.david.pda.util.time.DateUtil;
import com.david.pda.weather.model.Index;
import com.david.pda.weather.model.WeatherData;
import com.david.pda.weather.model.WeatherResult;
import com.david.pda.weather.model.util.L;
import com.david.pda.weather.model.util.WeatherResultUtil;

public class SomeToolsWeatherActivity extends Activity {
	GridView indexView;
	WeatherResult weatherResult;
	TextView cityText;
	TextView pmText;
	TextView dateText;
	TextView windText;
	TextView temperatureText;
	TextView weatherText;
	ImageView weatherPicView;
	ImageButton backwardImageButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.main_some_tools_weather_grid);
		// 详见StrictMode文档
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // or
				.penaltyLog().build());
		// textView = (TextView) findViewById(R.id.);
		getWeatherHandler.post(getWeatherTread);
		indexView = (GridView) findViewById(R.id.main_some_tools_weather_indexgrid);
		cityText = (TextView) findViewById(R.id.main_some_tools_weather_city);
		pmText = (TextView) findViewById(R.id.main_some_tools_weather_pm);
		dateText = (TextView) findViewById(R.id.main_some_tools_weather_date);
		windText = (TextView) findViewById(R.id.main_some_tools_weather_wind);
		temperatureText = (TextView) findViewById(R.id.main_some_tools_weather_temperature);
		weatherText = (TextView) findViewById(R.id.main_some_tools_weather_weather);
		weatherPicView = (ImageView) findViewById(R.id.main_some_tools_weather_weatherPic);
		backwardImageButton = (ImageButton)findViewById(R.id.main_some_tools_weather_topbar_backward);
		Bind.bindReturn(backwardImageButton, this, MainActivity.class, MainActivity.POSTION_SOME_TOOLS);
	}

	HandlerThread getWeatherTread = new HandlerThread("getWeather") {

		@Override
		public void run() {
			Log.i(L.t, "mmmmmmmmmmmmmmmmmmmmmmmmm");
			WeatherResult wr = WeatherResultUtil.getWeather("上海");
			Message m = getWeatherHandler.obtainMessage();
			m.obj = wr;
			getWeatherHandler.sendMessage(m);
			getWeatherHandler.removeCallbacks(getWeatherTread);
		}

	};

	@SuppressLint("HandlerLeak")
	Handler getWeatherHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			weatherResult = (WeatherResult) msg.obj;
			cityText.setText(weatherResult.getWeather()[0].getCity());
			cityText.setOnClickListener(new CityTextOnclickListener());
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
			showWeatherToView();
		}
	};

	public void showWeatherToView() {
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
		SimpleAdapter saImageItems = new SimpleAdapter(this, arryList,
				R.layout.grid_item, new String[] { "image", "text" },
				new int[] { R.id.grid_item_image, R.id.grid_item_text });
		indexView.setAdapter(saImageItems);// 添加适配器
		indexView.setOnItemClickListener(new IndexViewOnItemClickListener());
	}
	class CityTextOnclickListener implements OnClickListener{
		@SuppressLint("ShowToast")
		@Override
		public void onClick(View view) {
			Log.i(L.t, "dateTextView is clicked");
			WeatherData[] data = weatherResult.getWeather()[0].getWeather_data();
			Toast.makeText(SomeToolsWeatherActivity.this, data[0].getDate(),
					Toast.LENGTH_SHORT).show();
		}
	}
	class DateTextOnClickListener implements OnClickListener {
		@SuppressLint("ShowToast")
		@Override
		
		public void onClick(View view) {
			Log.i(L.t, "dateTextView is clicked");
			WeatherData[] data = weatherResult.getWeather()[0].getWeather_data();
			Toast.makeText(SomeToolsWeatherActivity.this, data[0].getDate(),
					Toast.LENGTH_SHORT).show();
			
			 Intent intent = new Intent(SomeToolsWeatherActivity.this,
			 SomeToolsWeatherDataActivity.class);
			 intent.putExtra("weather_data",data);
			 startActivity(intent);
			 SomeToolsWeatherActivity.this.finish();
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
					Toast.LENGTH_LONG).show();
			// Intent intent = new Intent(SomeToolsWeatherActivity.this,
			// SomeToolsWeatherIndexActivity.class);
			// intent.putExtra("des", index.getDes());
			// intent.putExtra("tipt", index.getTipt());
			// intent.putExtra("zs", index.getZs());
			// intent.putExtra("title", index.getTitle());
			// startActivity(intent);
		}

	}
}
