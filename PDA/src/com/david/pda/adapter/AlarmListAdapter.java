package com.david.pda.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.david.pda.R;
import com.david.pda.sqlite.model.Alarm;
import com.david.pda.sqlite.model.CycleDetailsForAlarm;
import com.david.pda.sqlite.model.CycleType;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.DateUtil;
import com.david.pda.util.time.CycleTipUtil;
import com.david.pda.weather.model.util.L;

public class AlarmListAdapter extends ArrayAdapter<Alarm> {
	final List<Alarm> alarms = new ArrayList<Alarm>();
	Context ctx;

	public AlarmListAdapter(Context ctx, List<Alarm> alarms) {
		super(ctx, R.id.list_item_alarm_title);
		this.alarms.clear();
		this.alarms.addAll(alarms);
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return alarms.size();
	}

	@Override
	public Alarm getItem(int arg0) {
		return alarms.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return alarms.get(arg0).get_id();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.list_item_alarm, parent, false);
		} else {
			row = convertView;
		}
		Alarm i = alarms.get(position);
		TextView t1 = (TextView) row.findViewById(R.id.list_item_alarm_title);
		t1.setText(i.getTitle());

		TextView t2 = (TextView) row.findViewById(R.id.list_item_alarm_cycle);
		long cycleTypeId = i.getCycleType();
		CycleType c = new DemoDB<CycleType>(new CycleType()).get(cycleTypeId
				+ "", ctx);
		t2.setText(c.getDescription());
		TextView t3 = (TextView) row.findViewById(R.id.list_item_alarm_remarks);
		t3.setText(i.getRemarks());
		TextView t4 = (TextView) row
				.findViewById(R.id.list_item_alarm_next_time);
		DemoDB<CycleDetailsForAlarm> db = new DemoDB<CycleDetailsForAlarm>(
				new CycleDetailsForAlarm());
		List<CycleDetailsForAlarm> details = db.getList(ctx, " cycleFor=?",
				new String[] { i.get_id() + "" }, null);
		CycleType ct = new DemoDB<CycleType>(new CycleType()).get(
				i.getCycleType() + "", ctx);
		Log.i(L.t, "hh:" + DateUtil.formatyyyy_MM_dd_HH_mm(i.getCreateTime()));
		Log.i(L.t,
				"now:"
						+ DateUtil.formatyyyy_MM_dd_HH_mm(System
								.currentTimeMillis()));
		CycleTipUtil ctu = new CycleTipUtil(details, ct, i.getCreateTime());
		CycleDetailsForAlarm cd = ctu.getNextTipDetail();
		t4.setText(DateUtil.formatMM_dd_HH_mm(cd.getStartTime()
				- cd.getAheadTime())
				+ (cd.getIsAhead() == Model.IS_YES ? "(提前" + cd.getAheadTime()
						/ 1000l / 60 + "分钟)" : ""));
		Log.i(L.t,
				"hh2:"
						+ DateUtil.formatyyyy_MM_dd_HH_mm(cd.getStartTime()
								- cd.getAheadTime()));
		return row;
	}
}
