package com.david.pda.util.other;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.david.pda.weather.model.util.L;

public class DateUtil {
	public final static String yyyyMMdd = "yyyyMMdd";
	public final static String yyyy_MM_dd = "yyyy-MM-dd";
	public final static String yyyy年MM月dd日 = "yyyy年MM月dd日";
	public final static String yyyy年M月d日 = "yyyy年M月d日";
	public final static String yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
	public final static String yyyy_MM_dd_hh_mm = "yyyy-MM-dd hh:mm";
	public final static String MM_dd_hh_mm = "MM-dd hh:mm";
	public final static String hh_mm_ss = "hh:mm:ss";
	public final static String hh_mm = "hh:mm";
	public final static String h时_m分 = "h时m分";

	@SuppressLint("SimpleDateFormat")
	public static String formatYYYY_MM_DD(long mi) {
		SimpleDateFormat sdf = new SimpleDateFormat(yyyy_MM_dd);
		return sdf.format(new Date(mi));
	}

	@SuppressLint("SimpleDateFormat")
	public static String formatyyyy_MM_dd_hh_mm_ss(long mi) {
		SimpleDateFormat sdf = new SimpleDateFormat(yyyy_MM_dd_hh_mm_ss);
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
	public static String formatMM_dd_hh_mm(Long createTime) {
		return new SimpleDateFormat(MM_dd_hh_mm).format(new Date(createTime));
	}

	@SuppressLint("SimpleDateFormat")
	public static String formatyyyy_MM_dd_hh_mm(Long createTime) {
		return new SimpleDateFormat(yyyy_MM_dd_hh_mm).format(new Date(
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
		Log.i("gap:", gap + "");
		Log.i("gap day:", gap / (1000 * 60 * 60 * 24) + "");
		Log.i("compareTime / gap:", "" + (compareTime / gap));
		return (int) (360 * (1.0 / (compareTime / gap)));
	}

	public static Paint getPaint(int angle) {
		Paint p = new Paint();
		if (angle > 360) {
			p.setColor(Color.GRAY);
			p.setStyle(Paint.Style.STROKE);
		} else if (angle > 180) {
			p.setColor(Color.YELLOW);
			p.setStyle(Paint.Style.STROKE);
		} else if (angle > 90) {
			p.setARGB(50, 0, 0, 255);
			p.setStyle(Paint.Style.FILL);
		} else if (angle > 30) {
			p.setARGB(50, 0, 0, 255);
			p.setStyle(Paint.Style.FILL);
		} else if (angle > 10) {
			p.setARGB(100, 0, 255, 0);
			p.setStyle(Paint.Style.FILL);
		} else if (angle >= 0) {
			p.setARGB(150, 255, 0, 0);
			p.setStyle(Paint.Style.FILL);
		} else {
			p.setARGB(10, 0, 0, 0);
			p.setStyle(Paint.Style.STROKE);
		}
		return p;
	}

	@SuppressLint("SimpleDateFormat")
	public static Long parse(String text, String yyyyMmDdHhMm) {
		if (yyyyMmDdHhMm != null && !yyyyMmDdHhMm.equals("")) {
			try {
				return new SimpleDateFormat(yyyyMmDdHhMm).parse(text).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return new Date().getTime();
	}
}
