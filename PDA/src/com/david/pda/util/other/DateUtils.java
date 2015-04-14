package com.david.pda.util.other;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class DateUtils {
	public final static String yyyymmdd = "yyyyMMdd";
	public final static String yyyy_mm_dd = "yyyy-MM-dd";
	public final static String yyyy年mm月dd日 = "yyyy年MM月dd日";
	public final static String yyyy年m月d日 = "yyyy年M月d日";
	public final static String yyyy_mm_dd_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
	public final static String hh_mm_ss = "hh:mm:ss";
	public final static String hh_mm = "hh:mm";
	public final static String h时_m分 = "h时m分";

	@SuppressLint("SimpleDateFormat")
	public static String formatYYYYMMDD(long me) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date(me));
	}

	@SuppressLint("SimpleDateFormat")
	public static String format(String partten, long me) {
		SimpleDateFormat sdf = new SimpleDateFormat(partten);
		return sdf.format(new Date(me));
	}
}
