package com.david.pda;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.david.pda.service.AlarmService;
import com.david.pda.sqlite.model.Alarm;
import com.david.pda.sqlite.model.CycleDetailsForAlarm;
import com.david.pda.sqlite.model.Plan;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;

public class TestActivity extends Activity {
	public static String ACTION_PALY = "com.david.pda.PLAY_ACTION";
	private Button bpause, bstop;
	Intent intent;
	private TextView title;
	private TextView content;
	Plan p;
	Alarm a;
	CycleDetailsForAlarm ca;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tip);
		p = null;
		a = null;
		ca = null;
		intent = getIntent();
		title = (TextView) findViewById(R.id.test_title);
		content = (TextView) findViewById(R.id.test_content);
		if (intent.hasExtra("plan")) {
			Plan p = (Plan) intent.getSerializableExtra("plan");
			title.setText(p.getTitle());
			content.setText(p.getCycleTypeObj().getDescription());
		} else if (intent.hasExtra("alarm")
				&& intent.hasExtra("cycledetailsforalarm")) {
			a = (Alarm) intent.getSerializableExtra("alarm");
			ca = (CycleDetailsForAlarm) intent
					.getSerializableExtra("cycledetailsforalarm");
			title.setText(a.getTitle());
			content.setText(ca.getDiscription());
		}

		// 动态注册广播接收器
		bpause = (Button) findViewById(R.id.pause);// cancel
		bstop = (Button) findViewById(R.id.stop);

		bpause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isFromService()) {
					Intent intent = new Intent();
					intent.setFlags(2);
					intent.setClass(TestActivity.this, AlarmService.class);
					TestActivity.this.startService(intent);
				}
				TestActivity.this.finish();
			}
		});

		bstop.setOnClickListener(new OnClickListener() {// update
			@Override
			public void onClick(View v) {
				if (a != null && ca != null) {
					ca.setIsTip(Model.IS_NO);
					DemoDB<CycleDetailsForAlarm> cadb = new DemoDB<CycleDetailsForAlarm>(
							ca);
					try {
						cadb.update(ca, TestActivity.this);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (p != null) {
					DemoDB<Plan> pdb = new DemoDB<Plan>(p);
					p.setIsTip(Model.IS_NO);
					try {
						pdb.update(p, TestActivity.this);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if (isFromService()) {
					Intent intent = new Intent();
					intent.setFlags(2);
					intent.setClass(TestActivity.this, AlarmService.class);
					TestActivity.this.startService(intent);
				}
				TestActivity.this.finish();
			}
		});
	}

	boolean isFromService() {
		return (a != null && ca != null || p != null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
