package com.david.pda.weather.model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.david.pda.weather.model.Index;
import com.david.pda.weather.model.Weather;
import com.david.pda.weather.model.WeatherData;
import com.david.pda.weather.model.WeatherResult;

//{"status":200,"message":"APP不存在，AK有误请检查再重试"}
public class WeatherResultUtil {
	private final static String AK = "FUXYMSrDjWT31lGBk8UIIZRP";
	private final static String URL = "http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=FUXYMSrDjWT31lGBk8UIIZRP";

	// 百度天气API
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

	// 根据城市获取天气信息的java代码
	// cityName 是你要取得天气信息的城市的中文名字，如“北京”，“深圳”
	static String getWeatherInform(String cityName) {
		String baiduUrl = getUrl(cityName);
		StringBuffer strBuf = new StringBuffer();
		try {
			URL url = new URL(baiduUrl);
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));// 转码。
			String line = null;
			while ((line = reader.readLine()) != null)
				strBuf.append(line + " ");
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	public static WeatherResult convertBean(String jsonStr) {
		WeatherResult obj = new WeatherResult();
		try {
			JSONObject json = new JSONObject(jsonStr);
			JSONArray results = json.getJSONArray("results");
			obj.setStatus(json.getString("status"));
			if ("success".equals(obj.getStatus())) {
				obj.setDate(json.getString("date"));
				obj.setError(json.getInt("error"));
				Weather[] weather = new Weather[results.length()];
				for (int i = 0; i < results.length(); i++) {
					weather[i] = new Weather();
					JSONObject result = results.getJSONObject(i);
					weather[i].setCity(result.getString("currentCity"));
					weather[i].setPm25(result.getString("pm25"));
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
					weather[i].setIndex(indexArr);
					weather[i].setWeather_data(weatherDataArr);
				}
				obj.setWeather(weather);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
