package com.david.pda;

import com.david.pda.application.SysApplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class TestDrawableActivity extends Activity {
	ImageView drawableImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_drawable);
		SysApplication.getInstance().addActivity(this);
		drawableImageView = (ImageView) findViewById(R.id.test_drawableImageView);
		drawableImageView.setBackgroundResource(R.drawable.ic_drawer);
	}
}
