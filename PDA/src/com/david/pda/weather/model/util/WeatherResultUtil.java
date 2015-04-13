package com.david.pda.weather.model.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.david.pda.util.net.GetUtil;
import com.david.pda.weather.model.Index;
import com.david.pda.weather.model.Weather;
import com.david.pda.weather.model.WeatherData;
import com.david.pda.weather.model.WeatherResult;

public class WeatherResultUtil {
	private final static String AK = "FUXYMSrDjWT31lGBk8UIIZRP";
	private final static String URL = "http://api.map.baidu.com/telematics/v3/weather?location=上海&output=json&ak=FUXYMSrDjWT31lGBk8UIIZRP";

	private static String getUrl(String cityName) {
		String baiduUrl = URL;
		try {
			baiduUrl = "http://api.map.baidu.com/telematics/v3/weather?location="
					+ URLEncoder.encode(cityName, "utf-8")
					+ "&output=json&ak="
					+ AK;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return baiduUrl;
	}

	public static String getWeatherStr(String cityName) {
		String uri = getUrl(cityName);
		return GetUtil.getForString(uri);
	}
	public static WeatherResult getWeather(String cityName){
		return convertBean(getWeatherStr(cityName));
	}
	public static WeatherResult convertBean(String jsonStr) {
		WeatherResult weatherResult = new WeatherResult();
		try {
			JSONObject json = new JSONObject(jsonStr);
			JSONArray results = json.getJSONArray("results");
			weatherResult.setStatus(json.getString("status"));
			if ("success".equals(weatherResult.getStatus())) {
				weatherResult.setDate(json.getString("date"));
				weatherResult.setError(json.getInt("error"));
				Weather[] weathers = new Weather[results.length()];
				for (int i = 0; i < results.length(); i++) {
					weathers[i] = new Weather();
					JSONObject result = results.getJSONObject(i);
					weathers[i].setCity(result.getString("currentCity"));
					weathers[i].setPm25(result.getString("pm25"));
					JSONArray index = result.getJSONArray("index");
					JSONArray weather_data = result
							.getJSONArray("weather_data");
					Index[] indexArr = new Index[index.length()];
					for (int j = 0; j < index.length(); j++) {
						JSONObject indexJson = index.getJSONObject(j);
						indexArr[j] = new Index();
						indexArr[j].setDes(indexJson.getString("des"));
						indexArr[j].setTitle(indexJson.getString("title"));
						indexArr[j].setZs(indexJson.getString("zs"));
						indexArr[j].setTipt(indexJson.getString("tipt"));
					}
					WeatherData[] weatherDataArr = new WeatherData[weather_data
							.length()];
					for (int k = 0; k < weather_data.length(); k++) {
						JSONObject weatherDataJson = weather_data
								.getJSONObject(k);
						weatherDataArr[k] = new WeatherData();
						weatherDataArr[k].setDate(weatherDataJson
								.getString("date"));
						weatherDataArr[k].setDayPictureUrl(weatherDataJson
								.getString("dayPictureUrl"));
						weatherDataArr[k].setNightPictureUrl(weatherDataJson
								.getString("nightPictureUrl"));
						weatherDataArr[k].setWeather(weatherDataJson
								.getString("weather"));
						weatherDataArr[k].setWind(weatherDataJson
								.getString("wind"));
						weatherDataArr[k].setTemperature(weatherDataJson
								.getString("temperature"));
					}
					weathers[i].setIndex(indexArr);
					weathers[i].setWeather_data(weatherDataArr);
				}
				weatherResult.setWeather(weathers);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return weatherResult;
	}
}
