package com.david.pda.weather.model;

public class Weather {

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	public Index[] getIndex() {
		return index;
	}

	public void setIndex(Index[] index) {
		this.index = index;
	}

	public WeatherData[] getWeather_data() {
		return weather_data;
	}

	public void setWeather_data(WeatherData[] weather_data) {
		this.weather_data = weather_data;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\t").append("city:").append(this.city).append('\n');
		sb.append("\t").append("pm25:").append(this.pm25).append('\n');
		sb.append("\t").append("index:").append('\n');
		for (Index i : index) {
			if (i != null)
				sb.append(i.toString()).append('\n');
		}
		sb.append("\t").append("weather_data:").append('\n');
		for (WeatherData w : weather_data) {
			if (w != null)
				sb.append(w.toString()).append('\n');
		}
		return sb.toString();
	}

	private String city;
	private String pm25;
	private Index[] index;
	private WeatherData[] weather_data;
}
