package weather.baidu;

public class WeatherData {
	private String date;
	private String dayPictureUrl;
	private String nightPictureUrl;
	private String weather;
	private String wind;
	private String temperature;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\t\t").append("date:").append(this.date).append('\n');
		sb.append("\t\t").append("dayPictureUrl:").append(this.dayPictureUrl)
				.append('\n');
		sb.append("\t\t").append("nightPictureUrl:")
				.append(this.nightPictureUrl).append('\n');
		sb.append("\t\t").append("weather:").append(this.weather)
				.append('\n');
		sb.append("\t\t").append("wind:").append(this.wind).append('\n');
		sb.append("\t\t").append("temperature:").append(this.temperature)
				.append('\n');
		return sb.toString();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDayPictureUrl() {
		return dayPictureUrl;
	}

	public void setDayPictureUrl(String dayPictureUrl) {
		this.dayPictureUrl = dayPictureUrl;
	}

	public String getNightPictureUrl() {
		return nightPictureUrl;
	}

	public void setNightPictureUrl(String nightPictureUrl) {
		this.nightPictureUrl = nightPictureUrl;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

}
