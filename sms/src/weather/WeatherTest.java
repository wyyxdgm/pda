package weather;

import weather.baidu.WeatherResult;
import weather.baidu.WeatherResultUtil;

public class WeatherTest {
	public static void main(String[] args) {
		String jsonStr = BaiduWeatherJson.getWeatherInform("上海");
		System.out.println(jsonStr);
		WeatherResult weatherResult = WeatherResultUtil.convertBean(jsonStr);
		System.out.println(weatherResult);
	}
}
