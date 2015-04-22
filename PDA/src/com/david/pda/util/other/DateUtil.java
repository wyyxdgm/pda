package com.david.pda.util.other;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.util.Log;

import com.david.pda.weather.model.util.L;

public class DateUtil {
	public final static long DAY = 24 * 60 * 60 * 1000l;
	public final static String yyyyMMdd = "yyyyMMdd";
	public final static String yyyy_MM_dd = "yyyy-MM-dd";
	public final static String yyyy年MM月dd日 = "yyyy年MM月dd日";
	public final static String yyyy年M月d日 = "yyyy年M月d日";
	public final static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public final static String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
	public final static String MM_dd_HH_mm = "MM-dd HH:mm";
	public final static String HH_mm_ss = "HH:mm:ss";
	public final static String HH_mm = "HH:mm";
	public final static String H时_m分 = "H时m分";

	@SuppressLint("SimpleDateFormat")
	public static String formatYYYY_MM_DD(long mi) {
		SimpleDateFormat sdf = new SimpleDateFormat(yyyy_MM_dd);
		return sdf.format(new Date(mi));
	}

	@SuppressLint("SimpleDateFormat")
	public static String formatyyyy_MM_dd_HH_mm_ss(long mi) {
		SimpleDateFormat sdf = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
		return sdf.format(new Date(mi));
	}

	@SuppressLint("SimpleDateFormat")
	public static String format(String partten, long me) {
		SimpleDateFormat sdf = new SimpleDateFormat(partten);
		return sdf.format(new Date(me));
	}

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

	@SuppressLint("SimpleDateFormat")
	public static String formatMM_dd_HH_mm(Long createTime) {
		return new SimpleDateFormat(MM_dd_HH_mm).format(new Date(createTime));
	}

	@SuppressLint("SimpleDateFormat")
	public static String formatyyyy_MM_dd_HH_mm(Long createTime) {
		return new SimpleDateFormat(yyyy_MM_dd_HH_mm).format(new Date(
				createTime));
	}

	// public static int getHoursFromNow(long mi) {
	// return (int) ((mi - System.currentTimeMillis()) / (1000 * 60 * 60));
	// }1471228928

	public static int getSweepAngle(long endTime, long compareTime) {
		Log.i(L.t, "endTime:" + endTime);
		Log.i(L.t, (endTime - System.currentTimeMillis()) / (1000 * 60 * 60)
				+ "");
		long gap = endTime - System.currentTimeMillis();
		if (gap < 0) {
			return -360;
		}
		Log.i(L.t, "gap:" + gap);
		Log.i(L.t, "compare day:" + (1.0 * compareTime / (1000 * 60 * 60 * 24)));
		Log.i(L.t, "gap day:" + (1.0 * gap / (1000 * 60 * 60 * 24)));
		Log.i(L.t, "compareTime / gap:" + (1.0 * compareTime / gap));
		return (int) (360 * (1.0 / (1.0 * compareTime / gap)));
	}

	public static Paint getPaint(int angle) {
		Paint p = new Paint();
		if (angle > 360) {
			p.setARGB(30, 5, 55, 5);
			p.setStyle(Paint.Style.FILL);
		} else if (angle > 180) {
			p.setARGB(50, 0, 50, 255);
			p.setStyle(Paint.Style.FILL);
		} else if (angle > 90) {
			p.setARGB(150, 250, 215, 0);
			p.setStyle(Paint.Style.FILL);
		} else if (angle > 50) {
			p.setARGB(150, 205, 50, 50);
			p.setStyle(Paint.Style.FILL);
		} else if (angle > 20) {
			p.setARGB(200, 250, 10, 10);
			p.setStyle(Paint.Style.FILL);
		} else if (angle >= 0) {
			p.setARGB(255, 255, 0, 0);
			p.setStyle(Paint.Style.FILL);
		} else {// 比当前早，kill it shuld be
			p.setARGB(10, 0, 0, 0);
			p.setStyle(Paint.Style.STROKE);
		}
		return p;
	}

	@SuppressLint("SimpleDateFormat")
	public static Long parse(String text, String yyyyMmDdHHMm) {
		if (yyyyMmDdHHMm != null && !yyyyMmDdHHMm.equals("")) {
			try {
				return new SimpleDateFormat(yyyyMmDdHHMm).parse(text).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return new Date().getTime();
	}

	public static String formatYMWD(Long cycleLength) {
		if (cycleLength > DAY)
			return new BigDecimal(cycleLength * 1.0 / DAY).setScale(2,
					BigDecimal.ROUND_HALF_EVEN).toString()
					+ "天";
		else {
			return format(H时_m分, cycleLength);
		}
	}
}
