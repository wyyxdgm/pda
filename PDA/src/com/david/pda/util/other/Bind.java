package com.david.pda.util.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.david.pda.weather.model.util.L;

public class Bind {
	public static void bindReturn(ImageButton ib,final Context context,final Class<?>target){
		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.i(L.t, "go back");
				Intent i = new Intent(context,target);
				context.startActivity(i);
				((Activity) context).finish();
			}
		});
	}
	public static void bindReturn(ImageButton ib,final Context context,final Class<?>target,final int flags){
		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.i(L.t, "go back");
				Intent i = new Intent(context,target);
				i.setFlags(flags);
				context.startActivity(i);
				((Activity) context).finish();
			}
		});
	}
}
