package com.david.pda.util.other;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

import com.david.pda.weather.model.util.L;

public class DateUtil {
	public final static long DAY = 24 * 60 * 60 * 1000l;
	public final static String yyyyMMdd = "yyyyMMdd";
	public final static String yyyy_MM_dd = "yyyy.MM.dd";
	public final static String yyyy年MM月dd日 = "yyyy年MM月dd日";
	public final static String yyyy年M月d日 = "yyyy年M月d日";
	public final static String yyyy_MM_dd_HH_mm_ss = "yyyy.MM.dd HH:mm:ss";
	public final static String yyyy_MM_dd_HH_mm = "yyyy.MM.dd HH:mm";
	public final static String MM_dd_HH_mm = "MM.dd HH:mm";
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

	public static long getTodayStartTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

	public static long getTodayEndTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.DAY_OF_MONTH, 1);
		return c.getTimeInMillis();
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
	public static String formatMM_dd_HH_mm(long createTime) {
		return new SimpleDateFormat(MM_dd_HH_mm).format(new Date(createTime));
	}

	@SuppressLint("SimpleDateFormat")
	public static String formatyyyy_MM_dd_HH_mm(long createTime) {
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

	@SuppressLint("SimpleDateFormat")
	public static long parsePT(String yyyyMmDdHHMm, String text) {
		if (yyyyMmDdHHMm != null && !yyyyMmDdHHMm.equals("")) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(yyyyMmDdHHMm);
				Date d = sdf.parse(text);
				return d.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return new Date().getTime();
	}

	public static String formatYMWD(long cycleLength) {
		if (cycleLength > DAY)
			return new BigDecimal(cycleLength * 1.0 / DAY).setScale(2,
					BigDecimal.ROUND_HALF_EVEN).toString()
					+ "天";
		else {
			return format(H时_m分, cycleLength);
		}
	}

	public static int[] getDHM(Long mi) {
		if (mi == null)
			return new int[] { 0, 0, 0 };
		int[] a = new int[3];
		long t = mi / (1000 * 60);// m is m
		a[2] = (int) (t % 60);// get m
		t /= 60;// m is h
		a[1] = (int) (t % 24);// get h
		a[0] = (int) (t / 24);// get day
		return a;
	}

	public static long getLongByDHM(int d, int h, int m) {
		return ((d * 24 + h) * 60 + m) * 60l * 1000l;
	}

	public static boolean isInOneMinute(long t, long currentTimeMillis) {
		return DateUtil.formatyyyy_MM_dd_HH_mm(t).equals(
				DateUtil.formatyyyy_MM_dd_HH_mm(currentTimeMillis));
	}
}
