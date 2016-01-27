package com.david.pda;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.david.pda.application.SysApplication;
import com.david.pda.service.AlarmService;
import com.david.pda.sqlite.model.Alarm;
import com.david.pda.sqlite.model.Countdown;
import com.david.pda.sqlite.model.CycleDetailsForAlarm;
import com.david.pda.sqlite.model.CycleDetailsForPlan;
import com.david.pda.sqlite.model.Plan;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.weather.model.WeatherResult;
import com.david.pda.weather.model.util.L;
import com.david.pda.weather.model.util.WeatherResultUtil;

public class TipActivity extends Activity {
	public static String ACTION_PALY = "com.david.pda.PLAY_ACTION";
	private Button cancelBtn, confirmBtn;
	Intent intent;
	private TextView title;
	private TextView content;
	private TextView weatherText;
	Plan p;
	CycleDetailsForPlan cp;
	Alarm a;
	CycleDetailsForAlarm ca;
	Countdown cd;
	private String weather = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tip);
		SysApplication.getInstance().addActivity(this);
		p = null;
		cp = null;
		a = null;
		ca = null;
		intent = getIntent();
		title = (TextView) findViewById(R.id.test_title);
		content = (TextView) findViewById(R.id.test_content);
		content = (TextView) findViewById(R.id.test_content);
		weatherText = (TextView) findViewById(R.id.test_weather);
		weather = intent.getStringExtra("weather");
		Log.i(L.t, "weather:" + (weather == null ? "null" : weather));
		String[] ew = null;
		if (intent.hasExtra("plan")) {
			p = (Plan) intent.getSerializableExtra("plan");
			cp = (CycleDetailsForPlan) intent
					.getSerializableExtra("cycledetailsforplan");
			title.setText("计划提醒：" + p.getTitle());
			content.setText(cp.getDiscription());
			ew = cp.getWeatherSensitivity() != null
					&& !"".equals(cp.getWeatherSensitivity()) ? cp
					.getWeatherSensitivity().split(",") : null;
		} else if (intent.hasExtra("alarm")
				&& intent.hasExtra("cycledetailsforalarm")) {
			a = (Alarm) intent.getSerializableExtra("alarm");
			ca = (CycleDetailsForAlarm) intent
					.getSerializableExtra("cycledetailsforalarm");
			title.setText("闹钟提醒：" + a.getTitle());
			content.setText(ca.getDiscription());
			ew = ca.getWeatherSensitivity() != null
					&& !"".equals(ca.getWeatherSensitivity()) ? ca
					.getWeatherSensitivity().split(",") : null;
		} else if (intent.hasExtra("countdown")) {
			cd = (Countdown) intent.getSerializableExtra("countdown");
			title.setText("倒计时提醒：" + cd.getTitle());
			content.setText(cd.getRemarks());
		}
		String wt = "";// weather text view text
		if (ew != null && ew.length > 0) {
			for (String sence : ew) {
				if (sence != null && !"".equals(sence) && weather != null
						&& !"".equals(weather) && weather.indexOf(sence) != -1) {
					WeatherResult wr = WeatherResultUtil.convertBean(weather);
					for (int i = 0; i < wr.getWeather()[0].getWeather_data().length; i++) {
						String sw = wr.getWeather()[0].getWeather_data()[0]
								.getWeather();
						if (sw.indexOf(sence) != -1) {
							wt = "天气:"
									+ sw
									+ "-"
									+ wr.getWeather()[0].getWeather_data()[0]
											.getDate();
							break;
						}
					}
				}
			}
		}
		if (wt != null) {
			weatherText.setText(wt);
		}
		// 动态注册广播接收器
		cancelBtn = (Button) findViewById(R.id.tip_cancel);// cancel
		confirmBtn = (Button) findViewById(R.id.tip_confirm);

		cancelBtn.setOnClickListener(new OnClickListener() {
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

		confirmBtn.setOnClickListener(new OnClickListener() {// update
					@Override
					public void onClick(View v) {
						if (a != null && ca != null) {
							// p = null;
							// cd = null;
							// ca.setIsTip(Model.IS_NO);
							// DemoDB<CycleDetailsForAlarm> cadb = new
							// DemoDB<CycleDetailsForAlarm>(
							// new CycleDetailsForAlarm());
							// try {
							// ca = cadb.get(ca.get_id() + "",
							// TipActivity.this);
							// ca.setIsTip(Model.IS_NO);
							// cadb.update(ca, TipActivity.this);
							// } catch (JSONException e) {
							// e.printStackTrace();
							// }
						} else if (p != null) {
							cd = null;
							a = null;
							p.setHashTipCycle(cp.getCycleNumber());
							DemoDB<Plan> pdb = new DemoDB<Plan>(new Plan());
							try {
								pdb.update(p, TipActivity.this);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else if (cd != null) {
							a = null;
							p = null;
							DemoDB<Countdown> cddb = new DemoDB<Countdown>(
									new Countdown());
							cd.setIsOn(Model.IS_NO);
							try {
								cddb.update(cd, TipActivity.this);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						if (isFromService()) {
							Intent intent = new Intent();
							intent.setFlags(2);
							intent.setClass(TipActivity.this,
									AlarmService.class);
							TipActivity.this.startService(intent);
						}
						TipActivity.this.finish();
					}
				});
	}

	boolean isFromService() {
		return (a != null && ca != null || p != null || cd != null);
	}

}
