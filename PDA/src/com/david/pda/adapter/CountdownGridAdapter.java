package com.david.pda.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.david.pda.R;
import com.david.pda.SomeToolsCountdownActivity;
import com.david.pda.sqlite.model.Countdown;
import com.david.pda.sqlite.model.base.Model;

public class CountdownGridAdapter extends BaseAdapter {
	private Context context;
	List<Countdown> data;

	public CountdownGridAdapter(Context context) {
		this.context = context;
		this.data = ((SomeToolsCountdownActivity) context).getData();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = null;
		if (convertView == null) {
			// create the row for the first time
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.grid_item_countdown, parent, false);
		} else {
			row = convertView;
		}
		TextView titleView = (TextView) row
				.findViewById(R.id.some_tools_countdown_item_title);
		ToggleButton flagView = (ToggleButton) row
				.findViewById(R.id.some_tools_countdown_item_isOn);
		ImageView endTime = (ImageView) row
				.findViewById(R.id.some_tools_countdown_item_endTime);

		Countdown countdown = data.get(position);
		if (countdown.getTitle() != null && countdown.getTitle().length() > 8) {
			titleView.setText(countdown.getTitle().subSequence(0, 6) + "...");
		} else {
			titleView.setText(countdown.getTitle());
		}
		flagView.setChecked(countdown.getIsOn() == Model.IS_NO);
		endTime.setImageResource(R.drawable.radio_button_on);
		return row;
	}
}
