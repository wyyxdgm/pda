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
import com.david.pda.sqlite.model.CycleType;
import com.david.pda.sqlite.model.util.DemoDB;

public class SystemSettingListAdapter extends BaseAdapter {
	private Context context;
	private List<CycleType> data = null;

	public SystemSettingListAdapter(Context context) {
		this.context = context;
		this.data = new DemoDB<CycleType>(new CycleType()).getList(context);
		if (this.data == null) {
			this.data = new ArrayList<CycleType>();
		}
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
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater
					.inflate(R.layout.list_item_cycle_type, parent, false);
		} else {
			row = convertView;
		}
		TextView name = (TextView) row
				.findViewById(R.id.list_item_cycle_type_name);
		TextView length = (TextView) row
				.findViewById(R.id.list_item_cycle_type_cycle_length);
		CycleType ct = data.get(position);
		name.setText(ct.getName());
		length.setText(ct.getCycleLength() + "" + ct.getDateType().getName());
		return row;
	}
}
