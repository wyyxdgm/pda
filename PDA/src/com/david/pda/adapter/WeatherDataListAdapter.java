package com.david.pda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.pda.R;
import com.david.pda.SomeToolsWeatherDataActivity;
import com.david.pda.util.net.OnLoadImageListener;
import com.david.pda.util.net.SyncDownload;
import com.david.pda.weather.model.WeatherData;

public class WeatherDataListAdapter extends BaseAdapter {
	private Context context;
	private WeatherData[] data;

	public WeatherDataListAdapter(Context context) {
		this.context = context;
		this.data = ((SomeToolsWeatherDataActivity) context).getData();
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int arg0) {
		return data[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = null;
		if (convertView == null) {
			// create the row for the first time
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.weather_data_list_item, parent,
					false);
		} else {
			row = convertView;
		}
		TextView dateTextView = (TextView) row
				.findViewById(R.id.weather_data_list_item_date);
		TextView temperatureTextView = (TextView) row
				.findViewById(R.id.weather_data_list_item_temperature);
		TextView weaterTextView = (TextView) row
				.findViewById(R.id.weather_data_list_item_weather);
		TextView windTextView = (TextView) row
				.findViewById(R.id.weather_data_list_item_wind);
		final ImageView weatherPicImageView = (ImageView) row
				.findViewById(R.id.weather_data_list_item_weatherPic);
		if (position == 0) {
			dateTextView.setText(data[position].getDate().subSequence(0, 10));
		} else {
			dateTextView.setText(data[position].getDate());
		}
		temperatureTextView.setText(data[position].getTemperature());
		weaterTextView.setText(data[position].getWeather());
		windTextView.setText(data[position].getWind());
		weatherPicImageView.setTag(data[position].getDayPictureUrl());
		SyncDownload.onLoadImage(data[position].getDayPictureUrl(),
				new OnLoadImageListener() {
					@Override
					public void OnLoadImage(Bitmap bitmap, String bitmapPath) {
						weatherPicImageView.setImageBitmap(bitmap);
					}
				});
		return row;
	}
}
