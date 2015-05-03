package com.david.pda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.david.pda.service.AlarmService;
import com.david.pda.sqlite.model.Plan;

public class TestActivity extends Activity {
	public static String ACTION_PALY = "com.david.pda.PLAY_ACTION";
	private Button bpause, bstop;
	Intent intent;
	private TextView title;
	private TextView content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		intent = getIntent();
		title = (TextView) findViewById(R.id.test_title);
		content = (TextView) findViewById(R.id.test_content);
		if (intent.hasExtra("plan")) {
			Plan p = (Plan) intent.getSerializableExtra("plan");
			title.setText(p.getTitle());
			content.setText(p.getCycleTypeObj().getDescription());
		}

		// 动态注册广播接收器
		bpause = (Button) findViewById(R.id.pause);
		bstop = (Button) findViewById(R.id.stop);

		bpause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setFlags(2);
				intent.setClass(TestActivity.this, AlarmService.class);
				TestActivity.this.startService(intent);
				TestActivity.this.finish();
			}
		});

		bstop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setFlags(2);
				intent.setClass(TestActivity.this, AlarmService.class);
				TestActivity.this.startService(intent);
				TestActivity.this.finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
