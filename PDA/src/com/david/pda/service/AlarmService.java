package com.david.pda.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.david.pda.R;
import com.david.pda.TestActivity;
import com.david.pda.sqlite.model.CycleDetailsForPlan;
import com.david.pda.sqlite.model.CycleType;
import com.david.pda.sqlite.model.Plan;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.time.CycleEntity;
import com.david.pda.weather.model.util.L;

public class AlarmService extends Service {
	private int i = 0;
	boolean isPlaying = true;
	MediaPlayer mediaPlayer = null;
	private int start;

	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(L.t, "onBind");
		return null;
	}

	@Override
	public void onCreate() {
		mediaPlayer = MediaPlayer.create(this, R.raw.music);
		mediaPlayer.setLooping(false);
		super.onCreate();
	}

	public void stop() {
		if (isPlaying) {
			mediaPlayer.pause();
			mediaPlayer.seekTo(start);
			isPlaying = false;
		}
	}

	public void play() {
		try {
			if (!isPlaying) {
				mediaPlayer.start();
				start = mediaPlayer.getCurrentPosition();
				isPlaying = true;
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
		if (i == 1) {
			play();
		} else if (i == 2) {
			stop();
		}
		Log.i(L.t, "flag from intent:" + i + "flags:" + flags + ",startId:"
				+ startId);
		return START_NOT_STICKY;
	}

	Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				Log.i(L.t, "第" + ++i + "次循环查询！");
				Plan p = GetTipPlan();
				if (p != null) {
					Intent intentv = new Intent(AlarmService.this,
							TestActivity.class);
					intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intentv.putExtra("plan", p);
					startActivity(intentv);
					play();
				}
				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	});
	List<CycleDetailsForPlan> ds;
	Map<Long, Plan> planMap;

	public Plan GetTipPlan() {
		CycleDetailsForPlan cc = null;
		doGet(System.currentTimeMillis(),
				System.currentTimeMillis() + 60 * 60 * 1000l);// 1 hour
		for (CycleDetailsForPlan c : ds) {
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

	public void doGet(long startTime, long endTime) {// 支持一个小时提前提醒
		DemoDB<Plan> db = new DemoDB<Plan>(new Plan());
		DemoDB<CycleDetailsForPlan> ddb = new DemoDB<CycleDetailsForPlan>(
				new CycleDetailsForPlan());
		List<CycleDetailsForPlan> details = ddb.getList(this, null, null,
				CycleDetailsForPlan.STARTTIME + " asc");
		Plan plan = null;
		DemoDB<CycleType> cdb = new DemoDB<CycleType>(new CycleType());
		List<Plan> plans = new ArrayList<Plan>();
		for (CycleDetailsForPlan d : details) {
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

}
