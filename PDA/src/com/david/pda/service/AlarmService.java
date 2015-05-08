package com.david.pda.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.david.pda.R;
import com.david.pda.SomeToolsAlarmActivity;
import com.david.pda.SomeToolsWeatherActivity;
import com.david.pda.TipActivity;
import com.david.pda.sqlite.model.Alarm;
import com.david.pda.sqlite.model.Countdown;
import com.david.pda.sqlite.model.CycleDetailsForAlarm;
import com.david.pda.sqlite.model.CycleDetailsForPlan;
import com.david.pda.sqlite.model.CycleType;
import com.david.pda.sqlite.model.Plan;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.DateUtil;
import com.david.pda.util.time.CycleEntity;
import com.david.pda.util.time.CycleTipUtil;
import com.david.pda.weather.model.util.L;
import com.david.pda.weather.model.util.WeatherResultUtil;

public class AlarmService extends Service {
	private int i = 0;
	boolean isPlaying = false;
	MediaPlayer mediaPlayer = null;
	private int start;
	private static String weatherInfo = "";

	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(L.t, "onBind");
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mediaPlayer = MediaPlayer.create(this, R.raw.music);
		mediaPlayer.setLooping(false);
		refreshWeatherThread.start();// 一直更新天气、per hour
		tipCountDownThread.start();
	}

	public void getWeather() {
		SharedPreferences sp = getSharedPreferences(
				SomeToolsAlarmActivity.class.getName(), MODE_MULTI_PROCESS);
		String city = sp.getString(SomeToolsWeatherActivity.DEFAULT_CITY,
				SomeToolsWeatherActivity.DEFAULTCYTY);
		String w = WeatherResultUtil.getWeatherStr(city);
		if (w != null) {
			weatherInfo = w;
		} else {
			Log.i(L.t, "无法访问网络，天气查询失败@AlarmService");
		}
	}

	public void stop() {
		if (isPlaying) {
			mediaPlayer.pause();
			mediaPlayer.seekTo(start);
			isPlaying = !isPlaying;
		}
	}

	public void play() {
		try {
			if (!isPlaying) {
				mediaPlayer.start();
				start = mediaPlayer.getCurrentPosition();
				isPlaying = !isPlaying;
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!thread.isAlive()) {
			thread.start();
		}
		int i = intent.getFlags();
		if (i == 1 || i == 2) {
			stop();
		} else if (i == 3) {
			play();
		}
		Log.i(L.t, "flag from intent:" + i + "flags:" + flags + ",startId:"
				+ startId);
		return START_NOT_STICKY;
	}

	private void tipPlan() {
		Plan p = GetTipPlan();
		if (p != null) {
			play();
			Intent intentv = new Intent(AlarmService.this, TipActivity.class);
			intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intentv.putExtra("plan", p);
			intentv.putExtra("weather", weatherInfo);
			startActivity(intentv);
		} else {
			Log.i(L.t, "ds size:" + ds.size());
		}
		try {
			Thread.sleep(30 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void tipAlarm() {
		CycleDetailsForAlarm ca = GetTipAlarm();
		if (ca != null) {
			play();
			Intent intentv = new Intent(AlarmService.this, TipActivity.class);
			intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intentv.putExtra("cycledetailsforalarm", ca);
			DemoDB<Alarm> db = new DemoDB<Alarm>(new Alarm());
			intentv.putExtra("alarm", db.get(ca.getCycleFor() + "", this));
			intentv.putExtra("weather", weatherInfo == null ? "" : weatherInfo);
			startActivity(intentv);
		} else {
			Log.i(L.t, "no alarm。。should be tiped");
		}
		try {
			Thread.sleep(30 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	Thread refreshWeatherThread = new Thread(new Runnable() {

		@Override
		public void run() {
			getWeather();
			try {
				Thread.sleep(1000 * 60 * 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	});
	Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				Log.i(L.t, "第" + ++i + "次循环查询！");
				tipPlan();
				tipAlarm();
			}
		}
	});
	Thread tipCountDownThread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				Countdown c = getCountDown();
				if (c != null) {
					play();
					Intent intentv = new Intent(AlarmService.this, TipActivity.class);
					intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intentv.putExtra("countdown", c);
					startActivity(intentv);
				}
			}
		}
	});
	List<CycleDetailsForPlan> ds;
	List<CycleDetailsForAlarm> alarmDetailList;
	Map<Long, Plan> planMap;
	Map<Long, Alarm> alarmMap;

	public Plan GetTipPlan() {
		CycleDetailsForPlan cc = null;
		doGet(System.currentTimeMillis(),
				System.currentTimeMillis() + 60 * 60 * 1000l);// 1 hour
		for (CycleDetailsForPlan c : ds) {
			Log.i(L.t,
					c.getDiscription()
							+ ",id:"
							+ c.get_id()
							+ ",plan:"
							+ (planMap.get(c.getCycleFor()) != null ? planMap
									.get(c.getCycleFor()).getTitle() : ""));
			if (c.getStartTime() - c.getAheadTime() < System
					.currentTimeMillis() + 119 * 1000l) {
				cc = c;
				break;
			}
		}
		if (cc != null) {
			return planMap.get(cc.getCycleFor());
		} else {
			return null;
		}
	}

	private CycleDetailsForAlarm GetTipAlarm() {
		DemoDB<Alarm> db = new DemoDB<Alarm>(new Alarm());
		List<Alarm> list = db.getList(this);
		DemoDB<CycleDetailsForAlarm> db2 = new DemoDB<CycleDetailsForAlarm>(
				new CycleDetailsForAlarm());
		for (Alarm i : list) {
			List<CycleDetailsForAlarm> details = db2.getList(this,
					" cycleFor=?", new String[] { i.get_id() + "" }, null);
			CycleType ct = new DemoDB<CycleType>(new CycleType()).get(
					i.getCycleType() + "", this);
			Log.i(L.t,
					"hh:" + DateUtil.formatyyyy_MM_dd_HH_mm(i.getCreateTime()));
			Log.i(L.t,
					"now:"
							+ DateUtil.formatyyyy_MM_dd_HH_mm(System
									.currentTimeMillis()));
			CycleTipUtil ctu = new CycleTipUtil(details, ct, i.getCreateTime());
			CycleDetailsForAlarm cd = ctu.getNextTipDetail();
			long t = (cd.getIsAhead() == Model.IS_YES ? (cd.getStartTime() - cd
					.getAheadTime()) : cd.getStartTime());
			Log.i(L.t, "xxxx" + DateUtil.formatyyyy_MM_dd_HH_mm(t));
			Log.i(L.t,
					DateUtil.formatyyyy_MM_dd_HH_mm(System.currentTimeMillis()));
			Log.i(L.t,
					"true?:"
							+ DateUtil.isInOneMinute(t,
									System.currentTimeMillis()));
			if (cd != null
					&& DateUtil.isInOneMinute(t, System.currentTimeMillis()))
				return cd;
		}
		return null;
	}

	// public CycleDetailsForAlarm GetTipAlarm() {
	// CycleDetailsForAlarm cc = null;
	// doGetAlarm(System.currentTimeMillis(),
	// System.currentTimeMillis() + 60 * 60 * 1000l);// 1 hour
	// for (CycleDetailsForAlarm c : alarmDetailList) {
	// Log.i(L.t,
	// c.getDiscription()
	// + ",id:"
	// + c.get_id()
	// + ",plan:"
	// + (alarmMap.get(c.getCycleFor()) != null ? alarmMap
	// .get(c.getCycleFor()).getTitle() : ""));
	// if (c.getStartTime() - c.getAheadTime() < System
	// .currentTimeMillis() + 119 * 1000l) {
	// cc = c;
	// break;
	// }
	// }
	// return cc;
	// }

	public void doGet(long startTime, long endTime) {// 支持一个小时提前提醒
		DemoDB<Plan> db = new DemoDB<Plan>(new Plan());
		DemoDB<CycleDetailsForPlan> ddb = new DemoDB<CycleDetailsForPlan>(
				new CycleDetailsForPlan());
		List<CycleDetailsForPlan> details = ddb.getList(this, null, null,
				CycleDetailsForPlan.STARTTIME + " asc");
		Plan plan = null;
		DemoDB<CycleType> cdb = new DemoDB<CycleType>(new CycleType());
		List<Plan> plans = new ArrayList<Plan>();
		for (CycleDetailsForPlan d : details) {// FILL TO ALARM
			plan = db.get(d.getCycleFor() + "", this);
			plan.setDetail(d);
			plan.setCycleTypeObj(cdb.get(plan.getCycleType() + "", this));
			plans.add(plan);
		}
		Plan pl = null;
		if (ds == null) {
			ds = new ArrayList<CycleDetailsForPlan>();
		} else {
			ds.clear();
		}
		planMap = new HashMap<Long, Plan>();
		for (int i = 0; i < plans.size(); i++) {
			planMap.put(plans.get(i).get_id(), plans.get(i));
			pl = plans.get(i);
			CycleEntity<CycleDetailsForPlan> ce = new CycleEntity<CycleDetailsForPlan>(
					startTime, endTime, pl, pl.getCycleTypeObj(),
					pl.getDetail());
			ds.addAll(ce.getTimes());
		}
		Collections.sort(ds);
	}

	public Countdown getCountDown() {
		DemoDB<Countdown> db = new DemoDB<Countdown>(new Countdown());
		List<Countdown> ct = db.getList(this);
		for (Countdown countdown : ct) {
			if (countdown.getIsOn() == Model.IS_YES
					&& DateUtil.isInOneMinute(countdown.getEndTime(),
							System.currentTimeMillis())) {
				return countdown;
			}
		}
		return null;
	}
	// public void doGetAlarm(long startTime, long endTime) {//
	// 支持一个小时提前提醒?????????????????????????????
	// DemoDB<Alarm> db = new DemoDB<Alarm>(new Alarm());
	// DemoDB<CycleDetailsForAlarm> ddb = new DemoDB<CycleDetailsForAlarm>(
	// new CycleDetailsForAlarm());
	// List<CycleDetailsForAlarm> details = ddb.getList(this, null, null,
	// CycleDetailsForAlarm.STARTTIME + " asc");
	// Alarm alarm = null;
	// DemoDB<CycleType> cdb = new DemoDB<CycleType>(new CycleType());
	// List<Alarm> alarms = new ArrayList<Alarm>();
	// alarmMap = new HashMap<Long, Alarm>();
	// alarms = db.getList(this);
	// if (alarmDetailList == null) {
	// alarmDetailList = new ArrayList<CycleDetailsForAlarm>();
	// } else {
	// alarmDetailList.clear();
	// }
	// for (Alarm a : alarms) {
	// alarmMap.put(a.get_id(), a);
	// }
	// for (CycleDetailsForAlarm d : details) {
	// alarm = alarmMap.get(d.getCycleFor());
	// alarm.addDetail(d);
	// alarm.setCycleTypeObj(cdb.get(alarm.getCycleType() + "", this));
	// }// add details,cycleType to alarm
	// for (Long key : alarmMap.keySet()) {
	// alarm = alarmMap.get(key);
	// for (CycleDetailsForAlarm d : alarm.getDetails()) {
	// CycleEntity<CycleDetailsForAlarm> ce = new
	// CycleEntity<CycleDetailsForAlarm>(
	// startTime, endTime, alarm.getCycleTypeObj(), d);
	// alarmDetailList.addAll(ce.getTimes());
	// }
	// }
	// Collections.sort(alarmDetailList);
	// }

}
