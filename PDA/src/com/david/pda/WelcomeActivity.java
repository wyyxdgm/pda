package com.david.pda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.david.pda.weather.model.util.L;

public class WelcomeActivity extends Activity {
	ImageView welcomeImageView;
	Handler initHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
		welcomeImageView = (ImageView) findViewById(R.id.welcome_welcome_image);
		welcomeImageView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motion) {
				int count = motion.getPointerCount();
				for (int i = 0; i < count; i++) {
					Log.i(L.t, motion.getX(i) + "," + motion.getY(i));
				}
				Log.i(L.t, view.getId() + " is touched by " + count
						+ " pointer");
				return false;
			}
		});
		// initHandler.postDelayed(initThread, 3000);
		initHandler.post(initThread);
	}

	Runnable initThread = new Runnable() {
		@Override
		public void run() {
			Log.i(L.t, "has delay 3000 to init app,now start");
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			intent.setFlags(MainActivity.POSTION_TARGET_MANAGE);
			startActivity(intent);
			WelcomeActivity.this.finish();
		}
	};
}
