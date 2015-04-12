package com.david.pda.util.time;

import java.util.Date;

public class DateUtil {
	public static Date getSystemTime() {
		return new Date(System.currentTimeMillis());
	}

	public static boolean isNight() {
		int hour = (int) ((System.currentTimeMillis() / (60 * 60 * 1000)) % 24);
		if (hour < 6 || hour > 18)
			return true;
		return false;
	}

	public static boolean isDay() {
		return !isNight();
	}
}
