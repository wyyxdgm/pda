package com.david.pda.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.david.pda.R;
import com.david.pda.sqlite.model.CycleDetails;
import com.david.pda.sqlite.model.CycleDetailsForAlarm;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.util.other.DateUtil;
import com.david.pda.weather.model.util.L;

public class CycleDetailsArrayAdapter extends ArrayAdapter<CycleDetails> {
	Context context;
	List<CycleDetails> details;
	int resource;

	public CycleDetailsArrayAdapter(Context context, int resource) {
		super(context, resource);
		this.context = context;
		this.resource = resource;
		this.details = new ArrayList<CycleDetails>();
	}

	@SuppressWarnings("unchecked")
	public CycleDetailsArrayAdapter(Context context, int resource,
			List<? extends CycleDetails> details) {
		super(context, resource);
		this.details = (List<CycleDetails>) details;
		this.context = context;
		this.resource = resource;
		Log.i(L.t, "details.size():" + details.size());
	}

	@Override
	public void add(CycleDetails c) {
		details.add(c);
		Log.i(L.t, "add:" + c.getDiscription());
	}

	@Override
	public void addAll(Collection<? extends CycleDetails> collection) {
		details.addAll(collection);
		Log.i(L.t, "addAll:");
	}

	@Override
	public void clear() {
		details.clear();
		Log.i(L.t, "clear:");
	}

	@Override
	public Context getContext() {
		return this.context;
	}

	@Override
	public int getCount() {
		Log.i(L.t, "getCount:");
		return details.size();
	}

	@Override
	public CycleDetails getItem(int position) {
		return details.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	CycleDetails getCycleDetailsFromView(View row) {
		TextView sday = (TextView) row
				.findViewById(R.id.list_item_cycle_details_starttime_day);
		TextView eday = (TextView) row
				.findViewById(R.id.list_item_cycle_details_endtime_day);
		TextView shour = (TextView) row
				.findViewById(R.id.list_item_cycle_details_starttime_hour);
		TextView ehour = (TextView) row
				.findViewById(R.id.list_item_cycle_details_endtime_hour);
		TextView sminute = (TextView) row
				.findViewById(R.id.list_item_cycle_details_starttime_minute);
		TextView eminute = (TextView) row
				.findViewById(R.id.list_item_cycle_details_endtime_minute);

		CheckBox isahead = (CheckBox) row
				.findViewById(R.id.list_item_cycle_details_isaheadtime);
		CheckBox istip = (CheckBox) row
				.findViewById(R.id.list_item_cycle_details_istip);
		TextView content = (TextView) row
				.findViewById(R.id.list_item_cycle_details_content);
		TextView aheadtime = (TextView) row
				.findViewById(R.id.list_item_cycle_details_ahead_time);
		TextView weatherSensitive = (TextView) row
				.findViewById(R.id.list_item_cycle_details_weather_sensitive);

		CycleDetails detail = new CycleDetailsForAlarm();
		int ssday = (int) getNumber(sday.getText().toString());
		int esday = (int) getNumber(eday.getText().toString());
		int sshour = (int) getNumber(shour.getText().toString());
		int eshour = (int) getNumber(ehour.getText().toString());

		int ssminute = (int) getNumber(sminute.getText().toString());
		int esminute = (int) getNumber(eminute.getText().toString());

		detail.set_id(null);
		detail.setStartTime(DateUtil.getLongByDHM(ssday, sshour, ssminute));
		detail.setEndTime(DateUtil.getLongByDHM(esday, eshour, esminute));
		detail.setIsAhead(isahead.isChecked() ? Model.IS_YES : Model.IS_NO);
		detail.setIsTip(istip.isChecked() ? Model.IS_YES : Model.IS_NO);
		detail.setDiscription(content.getText().toString());
		detail.setAheadTime(60l*1000l*getNumber(aheadtime.getText().toString()));
		detail.setWeatherSensitivity(weatherSensitive.getText().toString());
		return detail;
	}

	private long getNumber(String s) {
		if (TextUtils.isDigitsOnly(s)) {
			return Long.valueOf(s);
		} else
			return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.list_item_cycle_details, parent,
					false);
		} else {
			row = convertView;
		}
		CycleDetailsForAlarm detail = (CycleDetailsForAlarm) details
				.get(position);
		TextView sday = (TextView) row
				.findViewById(R.id.list_item_cycle_details_starttime_day);
		TextView eday = (TextView) row
				.findViewById(R.id.list_item_cycle_details_endtime_day);
		TextView shour = (TextView) row
				.findViewById(R.id.list_item_cycle_details_starttime_hour);
		TextView ehour = (TextView) row
				.findViewById(R.id.list_item_cycle_details_endtime_hour);
		TextView sminute = (TextView) row
				.findViewById(R.id.list_item_cycle_details_starttime_minute);
		TextView eminute = (TextView) row
				.findViewById(R.id.list_item_cycle_details_endtime_minute);

		CheckBox isahead = (CheckBox) row
				.findViewById(R.id.list_item_cycle_details_isaheadtime);
		CheckBox istip = (CheckBox) row
				.findViewById(R.id.list_item_cycle_details_istip);
		TextView content = (TextView) row
				.findViewById(R.id.list_item_cycle_details_content);
		TextView aheadtime = (TextView) row
				.findViewById(R.id.list_item_cycle_details_ahead_time);
		TextView weatherSensitive = (TextView) row
				.findViewById(R.id.list_item_cycle_details_weather_sensitive);

		int s[] = DateUtil.getDHM(detail.getStartTime());
		int e[] = DateUtil.getDHM(detail.getEndTime());
		sday.setText(s[0] + "");
		shour.setText(s[1] + "");
		sminute.setText(s[2] + "");
		eday.setText(e[0] + "");
		ehour.setText(e[1] + "");
		eminute.setText(e[2] + "");
		isahead.setChecked(detail.getIsAhead() == Model.IS_YES);
		istip.setChecked(detail.getIsTip() == Model.IS_YES);
		content.setText(detail.getDiscription());
		aheadtime.setText(detail.getAheadTime()/60/1000 + "");
		weatherSensitive.setText(detail.getWeatherSensitivity());
		return row;
	}

	@Override
	public void remove(CycleDetails object) {
		details.remove(object);
	}

}
