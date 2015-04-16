package com.david.pda.util.other;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class DateUtil {
	public final static String yyyyMMdd = "yyyyMMdd";
	public final static String yyyy_MM_dd = "yyyy-MM-dd";
	public final static String yyyy年MM月dd日 = "yyyy年MM月dd日";
	public final static String yyyy年M月d日 = "yyyy年M月d日";
	public final static String yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
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

}
