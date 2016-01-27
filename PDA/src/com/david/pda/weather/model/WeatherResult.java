package com.david.pda.weather.model;

import java.io.Serializable;

public class WeatherResult implements Serializable{
	private static final long serialVersionUID = 1L;
	private int error;
	private String status;
	private String date;
	private Weather[] weather;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("error:").append(this.error).append('\n');
		sb.append("status:").append(this.status).append('\n');
		sb.append("date:").append(this.date).append('\n');
		sb.append("weather:").append('\n');
		for (Weather w : weather) {
			if (w != null)
				sb.append(w.toString()).append('\n');
		}
		return sb.toString();
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Weather[] getWeather() {
		return weather;
	}

	public void setWeather(Weather[] weather) {
		this.weather = weather;
	}

}
