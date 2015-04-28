package com.david.pda.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.david.pda.R;
import com.david.pda.sqlite.model.Alarm;
import com.david.pda.sqlite.model.CycleDetailsForAlarm;
import com.david.pda.sqlite.model.CycleType;
import com.david.pda.sqlite.model.util.DemoDB;

public class AlarmListAdapter extends BaseAdapter {
	List<Alarm> alarms = null;
	Context ctx;

	public AlarmListAdapter() {
		alarms = new ArrayList<Alarm>();
	}

	public AlarmListAdapter(Context ctx, List<Alarm> alarms) {
		this.alarms = alarms;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return alarms.size();
	}

	@Override
	public Object getItem(int arg0) {
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
		// List<CycleDetailsForAlarm> detials = db.getList(ctx, "", new String[]
		// {}, null);
		List<CycleDetailsForAlarm> details = db.getList(ctx, " cycleFor=?",
				new String[] { i.get_id() + "" }, null);
		t4.setText(c.getName() + ":" + c.getDescription() + "("
				+ details.size() + ")");
		return row;
	}
}