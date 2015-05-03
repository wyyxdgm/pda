package com.david.pda.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.david.pda.weather.model.util.L;

public class AlarmReceiver extends BroadcastReceiver {
	public static String ACTION_PALY = "com.david.pda.recevier.action.play";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(L.t, "alarmReciver onReceive");
		// AssetManager a = context.getAssets();
		// MediaPlayer mediaPlayer = new MediaPlayer();
		// try {
		// AssetFileDescriptor fileDescriptor = a.openFd("music.mp3");
		// mediaPlayer
		// .setDataSource(fileDescriptor.getFileDescriptor(),
		// fileDescriptor.getStartOffset(),
		// fileDescriptor.getLength());
		// mediaPlayer.prepare();
		// } catch (IllegalArgumentException e) {
		// e.printStackTrace();
		// } catch (IllegalStateException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

}
