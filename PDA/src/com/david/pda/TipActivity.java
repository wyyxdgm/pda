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
import com.david.pda.weather.model.WeatherResult;
import com.david.pda.weather.model.util.WeatherResultUtil;

public class TipActivity extends Activity {
	public static String ACTION_PALY = "com.david.pda.PLAY_ACTION";
	private Button bpause, bstop;
	Intent intent;
	private TextView title;
	private TextView content;
	private TextView weatherText;
	Plan p;
	Alarm a;
	CycleDetailsForAlarm ca;
	private String weather = "";

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
		content = (TextView) findViewById(R.id.test_content);
		weatherText = (TextView) findViewById(R.id.test_weather);
		weather = intent.getStringExtra("weather");
		String ew = "";
		if (intent.hasExtra("plan")) {
			p = (Plan) intent.getSerializableExtra("plan");
			title.setText(p.getTitle());
			content.setText(p.getCycleTypeObj().getDescription());
			ew = p.getWeatherSensitivity();
		} else if (intent.hasExtra("alarm")
				&& intent.hasExtra("cycledetailsforalarm")) {
			a = (Alarm) intent.getSerializableExtra("alarm");
			ca = (CycleDetailsForAlarm) intent
					.getSerializableExtra("cycledetailsforalarm");
			title.setText(a.getTitle());
			content.setText(ca.getDiscription());
			ew = p.getWeatherSensitivity();
		}
		String wt = "";// weather text view text
		if (ew != null && !"".equals(ew) && weather != null
				&& !"".equals(weather) && weather.indexOf(ew) != -1) {
			WeatherResult wr = WeatherResultUtil.convertBean(weather);
			for (int i = 0; i < wr.getWeather()[0].getWeather_data().length; i++) {
				String sw = wr.getWeather()[0].getWeather_data()[0]
						.getWeather();
				if (sw.indexOf(ew) != -1) {
					wt = "请注意天气:" + sw + "["
							+ wr.getWeather()[0].getWeather_data()[0].getDate()
							+ "]";
					break;
				}
			}
		}
		if (wt != null) {
			weatherText.setText(wt);
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
					intent.setClass(TipActivity.this, AlarmService.class);
					TipActivity.this.startService(intent);
				}
				TipActivity.this.finish();
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
						cadb.update(ca, TipActivity.this);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (p != null) {
					DemoDB<Plan> pdb = new DemoDB<Plan>(p);
					p.setIsTip(Model.IS_NO);
					try {
						pdb.update(p, TipActivity.this);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if (isFromService()) {
					Intent intent = new Intent();
					intent.setFlags(2);
					intent.setClass(TipActivity.this, AlarmService.class);
					TipActivity.this.startService(intent);
				}
				TipActivity.this.finish();
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
