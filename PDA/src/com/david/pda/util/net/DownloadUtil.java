package com.david.pda.util.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DownloadUtil {
	public static String downloadAsString(String urlStr) {
		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;
		InputStream input = null;
		HttpURLConnection conn;
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			input = conn.getInputStream();
			in = new BufferedReader(new InputStreamReader(input));
			String line = null;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static Bitmap getBitmap(String uri) {
		try {
			URL url = new URL(uri); 
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Bitmap bitmap = BitmapFactory.decodeStream(httpURLConnection
						.getInputStream());
				return bitmap;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
