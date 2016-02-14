package com.david.pda.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.content.Context;

public class LocalUtil {
	public static final String CITYS = "citys.txt";

	public static String getAssertFileToString(Context ctx, String file) {
		StringBuffer sb = new StringBuffer();
		InputStream is;
		BufferedReader in;
		try {
			is = ctx.getAssets().open(file);
			in = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = in.readLine()) != null) {
				sb.append(line).append('\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@SuppressLint("DefaultLocale") @SuppressWarnings("resource")
	public static String getCity(Context ctx, String key) {
		InputStream is = null;
		BufferedReader in = null;
		try {
			is = ctx.getAssets().open(CITYS);
			in = new BufferedReader(new InputStreamReader(is));
			String line = null;
			key = key.toLowerCase();
			while ((line = in.readLine()) != null) {
				if (line.toLowerCase().indexOf(key) != -1) {
					return line.split("\t")[1];
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
