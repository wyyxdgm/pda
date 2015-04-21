package com.david.pda;

import com.david.pda.util.other.Bind;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

public class AffairPlanOptionActivity extends Activity {
	ImageButton backward;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_affair_plan_option);
		backward = (ImageButton) findViewById(R.id.main_affair_plan_option_topbar_backward);
		Bind.bindReturn(backward, AffairPlanOptionActivity.this,
				MainActivity.class, MainActivity.POSTION_AFFAIR_PLAN);
	}
}
