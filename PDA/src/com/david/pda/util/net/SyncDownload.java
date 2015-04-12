package com.david.pda.util.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class SyncDownload {
	public static void onLoadImage(final String bitmapUrl,
			final OnLoadImageListener onLoadImageListener) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				onLoadImageListener.OnLoadImage((Bitmap) msg.obj, null);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL imageUrl = new URL(bitmapUrl);
					HttpURLConnection conn = (HttpURLConnection) imageUrl
							.openConnection();
					InputStream inputStream = conn.getInputStream();
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					Message msg = new Message();
					msg.obj = bitmap;
					handler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
