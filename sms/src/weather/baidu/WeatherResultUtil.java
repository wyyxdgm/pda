package weather.baidu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
		JSONObject json = JSONObject.fromObject(jsonStr);
		JSONArray results = json.getJSONArray("results");
		obj.setStatus(json.getString("status"));
		if ("success".equals(obj.getStatus())) {
			obj.setDate(json.getString("date"));
			obj.setError(json.getInt("error"));
			Weather[] weather = new Weather[results.size()];
			int i = -1;
			Iterator itresults = results.iterator();
			while (itresults.hasNext() && ++i > -1) {
				weather[i] = new Weather();
				JSONObject result = (JSONObject) itresults.next();

				weather[i].setCity(result.getString("currentCity"));
				weather[i].setPm25(result.getString("pm25"));
				JSONArray index = result.getJSONArray("index");
				JSONArray weather_data = result.getJSONArray("weather_data");
				int j = -1;
				Index[] indexArr = new Index[index.size()];
				Iterator itIndex = index.iterator();
				while (itIndex.hasNext() && ++j > -1) {
					JSONObject indexJson = (JSONObject) itIndex.next();
					indexArr[j] = new Index();
					indexArr[j].setDes(indexJson.getString("des"));
					indexArr[j].setTitle(indexJson.getString("title"));
					indexArr[j].setZs(indexJson.getString("zs"));
					indexArr[j].setTipt(indexJson.getString("tipt"));
				}
				int k = -1;
				WeatherData[] weatherDataArr = new WeatherData[weather_data
						.size()];
				Iterator itWeatherData = weather_data.iterator();
				while (itWeatherData.hasNext() && ++k > -1) {
					JSONObject weatherDataJson = (JSONObject) itWeatherData
							.next();
					weatherDataArr[k] = new WeatherData();
					weatherDataArr[k]
							.setDate(weatherDataJson.getString("date"));
					weatherDataArr[k].setDayPictureUrl(weatherDataJson
							.getString("dayPictureUrl"));
					weatherDataArr[k].setNightPictureUrl(weatherDataJson
							.getString("nightPictureUrl"));
					weatherDataArr[k].setWeather(weatherDataJson
							.getString("weather"));
					weatherDataArr[k]
							.setWind(weatherDataJson.getString("wind"));
					weatherDataArr[k].setTemperature(weatherDataJson
							.getString("temperature"));
				}
				weather[i].setIndex(indexArr);
				weather[i].setWeather_data(weatherDataArr);
			}
			obj.setWeather(weather);
			return obj;
		} else {
			return null;
		}

	}
}
