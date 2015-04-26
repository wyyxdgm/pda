package com.david.pda.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.david.pda.R;
import com.david.pda.sqlite.model.CycleDetails;
import com.david.pda.sqlite.model.CycleDetailsForAlarm;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.util.other.DateUtil;

public class CycleDetailsAdapter extends BaseAdapter {
	Context context;
	List<? extends CycleDetails> details;

	public CycleDetailsAdapter(Context context,
			List<? extends CycleDetails> details) {
		this.details = details;
	}

	@Override
	public int getCount() {
		return details.size();
	}

	@Override
	public Object getItem(int arg0) {
		return details.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return details.get(arg0).get_id();
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
		aheadtime.setText(detail.getAheadTime() + "");
		weatherSensitive.setText(detail.getWeatherSensitivity());
		return row;
	}

}
