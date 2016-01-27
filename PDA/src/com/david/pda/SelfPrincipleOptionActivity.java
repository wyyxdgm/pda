package com.david.pda;

import com.david.pda.R;
import com.david.pda.application.SysApplication;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

public class SelfPrincipleOptionActivity extends Activity {
	ImageButton backward;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_self_principle_option);
		SysApplication.getInstance().addActivity(this);
		backward = (ImageButton) findViewById(R.id.main_self_principle_option_topbar_backward);
		/*Bind.bindReturn(backward, SelfPrincipleOptionActivity.this,
				MainActivity.class, MainActivity.POSTION_SELF_PRINCIPLE);*/
	}
}
