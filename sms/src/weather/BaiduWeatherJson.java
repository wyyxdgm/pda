package weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class BaiduWeatherJson {
	private final static String ak = "FUXYMSrDjWT31lGBk8UIIZRP";
private final static String url = "http://api.map.baidu.com/telematics/v3/weather?location=%E5%AE%81%E6%B3%A2&output=json&ak=FUXYMSrDjWT31lGBk8UIIZRP";
	// 根据城市获取天气信息的java代码
	// cityName 是你要取得天气信息的城市的中文名字，如“北京”，“深圳”
	static String getWeatherInform(String cityName) {

		// 百度天气API
		String baiduUrl = "http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak="
				+ ak;
		StringBuffer strBuf;

		try {
			// 通过浏览器直接访问http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ
			// 5slgyqGDENN7Sy7pw29IUvrZ 是我自己申请的一个AK(许可码)，如果访问不了，可以自己去申请一个新的ak
			// 百度ak申请地址：http://lbsyun.baidu.com/apiconsole/key
			// 要访问的地址URL，通过URLEncoder.encode()函数对于中文进行转码
			baiduUrl = "http://api.map.baidu.com/telematics/v3/weather?location="
					+ URLEncoder.encode(cityName, "utf-8")
					+ "&output=json&ak="
					+ ak;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		strBuf = new StringBuffer();

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

	// 解析JSON数据 要解析的JSON数据:strPar
	// WeatherInf 是自己定义的承载所有的天气信息的实体类，包含了四天的天气信息。详细定义见后面。
	static WeatherInf resolveWeatherInf(String strPar) {

		JSONObject dataOfJson = JSONObject.fromObject(strPar);

		if (dataOfJson.getInt("error") != 0) {
			return null;
		}

		// 保存全部的天气信息。
		WeatherInf weatherInf = new WeatherInf();

		// 从json数据中取得的时间�?
		String date = dataOfJson.getString("date");
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(5, 7));
		int day = Integer.parseInt(date.substring(8, 10));
		Date today = new Date(year - 1900, month - 1, day);

		JSONArray results = dataOfJson.getJSONArray("results");
		JSONObject results0 = results.getJSONObject(0);

		String location = results0.getString("currentCity");
		int pmTwoPointFive;

		if (results0.getString("pm25").isEmpty()) {
			pmTwoPointFive = 0;
		} else {
			pmTwoPointFive = results0.getInt("pm25");
		}
		// System.out.println(results0.get("pm25").toString()+"11");

		try {

			JSONArray index = results0.getJSONArray("index");

			JSONObject index0 = index.getJSONObject(0);// 穿衣
			JSONObject index1 = index.getJSONObject(1);// 洗车
			JSONObject index2 = index.getJSONObject(2);// 感冒
			JSONObject index3 = index.getJSONObject(3);// 运动
			JSONObject index4 = index.getJSONObject(4);// 紫外线强度

			String dressAdvise = index0.getString("des");// 穿衣建议
			String washCarAdvise = index1.getString("des");// 洗车建议
			String coldAdvise = index2.getString("des");// 感冒建议
			String sportsAdvise = index3.getString("des");// 运动建议
			String ultravioletRaysAdvise = index4.getString("des");// 紫外线建议

			weatherInf.setDressAdvise(dressAdvise);
			weatherInf.setWashCarAdvise(washCarAdvise);
			weatherInf.setColdAdvise(coldAdvise);
			weatherInf.setSportsAdvise(sportsAdvise);
			weatherInf.setUltravioletRaysAdvise(ultravioletRaysAdvise);

		} catch (JSONException jsonExp) {

			weatherInf.setDressAdvise("要温度，也要风度。天热缓减衣，天凉及添衣！");
			weatherInf.setWashCarAdvise("你洗还是不洗，灰尘都在哪里，不增不减。");
			weatherInf.setColdAdvise("一天一个苹果，感冒不来找我！多吃水果和蔬菜。");
			weatherInf.setSportsAdvise("生命在于运动！不要总宅在家里哦！");
			weatherInf.setUltravioletRaysAdvise("心灵可以永远年轻，皮肤也一样可以！");
		}

		JSONArray weather_data = results0.getJSONArray("weather_data");// weather_data中有四项�?

		// OneDayWeatherInf是自己定义的承载某一天的天气信息的实体类，详细定义见后面。
		OneDayWeatherInf[] oneDayWeatherInfS = new OneDayWeatherInf[4];
		for (int i = 0; i < 4; i++) {
			oneDayWeatherInfS[i] = new OneDayWeatherInf();
		}

		for (int i = 0; i < weather_data.size(); i++) {

			JSONObject OneDayWeatherinfo = weather_data.getJSONObject(i);
			String dayData = OneDayWeatherinfo.getString("date");
			OneDayWeatherInf oneDayWeatherInf = new OneDayWeatherInf();

			oneDayWeatherInf.setDate((today.getYear() + 1900) + "."
					+ (today.getMonth() + 1) + "." + today.getDate());
			today.setDate(today.getDate() + 1);// 增加一天

			oneDayWeatherInf.setLocation(location);
			oneDayWeatherInf.setPmTwoPointFive(pmTwoPointFive);

			if (i == 0) {// 第一个，也就是当天的天气，在date字段中最后包含了实时天气
				int beginIndex = dayData.indexOf("：");
				int endIndex = dayData.indexOf(")");
				if (beginIndex > -1) {
					oneDayWeatherInf.setTempertureNow(dayData.substring(
							beginIndex + 1, endIndex));
					oneDayWeatherInf.setWeek(OneDayWeatherinfo
							.getString("date").substring(0, 2));
				} else {
					oneDayWeatherInf.setTempertureNow(" ");
					oneDayWeatherInf.setWeek(OneDayWeatherinfo
							.getString("date").substring(0, 2));
				}

			} else {
				oneDayWeatherInf.setWeek(OneDayWeatherinfo.getString("date"));
			}

			oneDayWeatherInf.setTempertureOfDay(OneDayWeatherinfo
					.getString("temperature"));
			oneDayWeatherInf.setWeather(OneDayWeatherinfo.getString("weather"));
			oneDayWeatherInf.setWind(OneDayWeatherinfo.getString("wind"));

			oneDayWeatherInfS[i] = oneDayWeatherInf;
		}

		weatherInf.setWeatherInfs(oneDayWeatherInfS);

		return weatherInf;
	}

}
