package com.david.pda;

import com.david.pda.application.SysApplication;

import android.app.Activity;
import android.os.Bundle;

public class AffairFourClass extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
	}
}
