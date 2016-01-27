package com.david.pda.receiver;

import com.david.pda.service.AlarmService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			Intent startAlarmService = new Intent(ctx, AlarmService.class);
			startAlarmService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ctx.startService(startAlarmService);
		}
	}
}
