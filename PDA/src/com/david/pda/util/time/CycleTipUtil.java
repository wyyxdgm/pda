package com.david.pda.util.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.david.pda.sqlite.model.CycleDetails;
import com.david.pda.sqlite.model.CycleDetailsForAlarm;
import com.david.pda.sqlite.model.CycleType;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.util.other.DateUtil;
import com.david.pda.weather.model.util.L;

public class CycleTipUtil {
	private List<CycleDetailsForAlarm> details;
	private CycleType cycleType;
	private long now = 0;
	private Calendar c = Calendar.getInstance();
	private Calendar cycleStart = Calendar.getInstance();// zhou qi qi shi
	private Calendar cycleEnd = Calendar.getInstance();// xia yi ge zhou qi jie
														// shu
	private int cycleLength;
	private long createTime;

	public CycleTipUtil(List<CycleDetailsForAlarm> details,
			CycleType cycleType, long createTime) {
		super();
		now = System.currentTimeMillis();
		Log.i(L.t, "now:" + DateUtil.formatyyyy_MM_dd_HH_mm_ss(now));
		this.details = details;
		this.cycleType = cycleType;
		this.cycleLength = cycleType == null
				|| cycleType.getCycleLength() == null ? 0 : cycleType
				.getCycleLength().intValue();
		this.createTime = createTime;
		getCurrentCycle();
	}

	private void getCurrentCycle() {
		// now can only be ealiear than cycle
		getCycleStartEndOfCreateTime();
		while (!(now < cycleEnd.getTimeInMillis() && now > cycleStart
				.getTimeInMillis())) {
			nextCycle();
		}
	}

	private void getCycleList() {// details chain to now + next Cycle
		List<CycleDetailsForAlarm> list = new ArrayList<CycleDetailsForAlarm>();
		for (CycleDetailsForAlarm cc : details) {
			CycleDetailsForAlarm c = (CycleDetailsForAlarm) cc.clone();
			long gap = c.getEndTime() - c.getStartTime();
			c.setStartTime(c.getStartTime() + cycleStart.getTimeInMillis());
			c.setEndTime(c.getStartTime() + gap);
			list.add(c);
		}
		nextCycle();// next Cycle
		for (CycleDetailsForAlarm cc : details) {
			CycleDetailsForAlarm c = (CycleDetailsForAlarm) cc.clone();
			long gap = c.getEndTime() - c.getStartTime();
			c.setStartTime(c.getStartTime() + cycleStart.getTimeInMillis());
			c.setEndTime(c.getStartTime() + gap);
			list.add(c);
		}
		details = list;
	}

	public CycleDetailsForAlarm getNextTipDetail() {
		long t = Long.MAX_VALUE;
		CycleDetailsForAlarm c = null;
		getCycleList();
		for (CycleDetailsForAlarm ac : details) {
			Log.i(L.t,
					DateUtil.formatyyyy_MM_dd_HH_mm_ss(ac.getStartTime())
							+ ";"
							+ DateUtil.formatyyyy_MM_dd_HH_mm_ss(ac
									.getStartTime() - ac.getAheadTime()));
			if (ac.getStartTime()
					- (ac.getIsAhead() == Model.IS_YES ? ac.getAheadTime() : 0) < now) {// 时间已经过去
				continue;
			}
			Log.i(L.t, "t:" + DateUtil.formatyyyy_MM_dd_HH_mm_ss(t));
			if (ac.getStartTime()
					- (ac.getIsAhead() == Model.IS_YES ? ac.getAheadTime() : 0) < t) {// get
																						// the
																						// smallest
				t = ac.getStartTime() - ac.getAheadTime();
				c = ac;
			}
		}
		return c;
	}

	private void getCycleStartEndOfCreateTime() {
		c.setTimeInMillis(createTime);
		switch (cycleType.getDateType()) {
		case DAY:// day start
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			cycleStart.setTimeInMillis(c.getTimeInMillis());
			Log.i(L.t,
					"cycle Start:"
							+ DateUtil.formatyyyy_MM_dd_HH_mm_ss(cycleStart
									.getTimeInMillis()));
			;
			c.add(Calendar.DATE, cycleLength);
			cycleEnd.setTimeInMillis(c.getTimeInMillis());
			Log.i(L.t,
					"cycle End:"
							+ DateUtil.formatyyyy_MM_dd_HH_mm_ss(cycleEnd
									.getTimeInMillis()));
			;
			break;
		case MONTH:// date of month start
			c.set(Calendar.DAY_OF_MONTH, 0);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			cycleStart.setTimeInMillis(c.getTimeInMillis());
			c.add(Calendar.MONTH, cycleLength);
			cycleEnd.setTimeInMillis(c.getTimeInMillis());
			break;
		case QUARTER:
			c.set(Calendar.DAY_OF_MONTH, 0);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			cycleStart.setTimeInMillis(c.getTimeInMillis());
			c.add(Calendar.MONTH, (cycleLength * 3));
			cycleEnd.setTimeInMillis(c.getTimeInMillis());
			break;
		case HOUR:
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			cycleStart.setTimeInMillis(c.getTimeInMillis());
			c.add(Calendar.HOUR_OF_DAY, cycleLength);
			cycleEnd.setTimeInMillis(c.getTimeInMillis());
			break;
		case WEEK:
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			while (c.get(Calendar.WEDNESDAY) != 0) {// zhou ri qi shi
				c.add(Calendar.DATE, -1);
			}
			cycleStart.setTimeInMillis(c.getTimeInMillis());
			c.add(Calendar.DATE, 7 * cycleLength);
			cycleEnd.setTimeInMillis(c.getTimeInMillis());
			break;
		case YEAR:
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 0);
			cycleStart.setTimeInMillis(c.getTimeInMillis());
			c.add(Calendar.YEAR, cycleLength);
			cycleEnd.setTimeInMillis(c.getTimeInMillis());
			break;
		default:
			System.err.println("Error DateType!");
			break;
		}
	}

	private void nextCycle() {
		switch (cycleType.getDateType()) {
		case DAY:
			c.add(Calendar.DATE, cycleLength);
			cycleStart.add(Calendar.DATE, cycleLength);
			cycleEnd.add(Calendar.DATE, cycleLength);
			break;
		case MONTH:
			c.add(Calendar.MONTH, cycleLength);
			cycleEnd.add(Calendar.MONTH, cycleLength);
			cycleStart.add(Calendar.MONTH, cycleLength);
			break;
		case QUARTER:
			c.add(Calendar.MONTH, (cycleLength * 3));
			cycleStart.add(Calendar.MONTH, (cycleLength * 3));
			cycleEnd.add(Calendar.MONTH, (cycleLength * 3));
			break;
		case HOUR:
			c.add(Calendar.HOUR, cycleLength);
			cycleStart.add(Calendar.HOUR, cycleLength);
			cycleEnd.add(Calendar.HOUR, cycleLength);
			break;
		case WEEK:
			c.add(Calendar.HOUR, cycleLength);
			cycleStart.add(Calendar.HOUR, cycleLength);
			cycleEnd.add(Calendar.HOUR, cycleLength);
			break;
		case YEAR:
			c.add(Calendar.HOUR, cycleLength);
			cycleStart.add(Calendar.HOUR, cycleLength);
			cycleEnd.add(Calendar.HOUR, cycleLength);
			break;
		default:
			System.err.println("Error DateType!");
			break;
		}
	}

	private List<CycleDetails> getDetailsTheseTwoCycle() {
		return null;
	}

	public CycleDetails getTheLastDetailToTip() {
		List<CycleDetails> list = getDetailsTheseTwoCycle();
		Collections.sort(list);

		return null;
	}

}
