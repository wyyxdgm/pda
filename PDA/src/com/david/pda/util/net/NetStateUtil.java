package com.david.pda.util.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetStateUtil {
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager m = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo[] networks = new NetworkInfo[] {
					m.getNetworkInfo(ConnectivityManager.TYPE_WIFI),
					m.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET),
					m.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) };
			for (NetworkInfo ni : networks) {
				if (ni != null && ni.isAvailable()) {
					return true;
				}
			}
		}
		return false;
	}

	public static void openWifi(Context context) {

	}
}
