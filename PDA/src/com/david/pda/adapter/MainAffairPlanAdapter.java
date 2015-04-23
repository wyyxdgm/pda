package com.david.pda.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.david.pda.R;
import com.david.pda.sqlite.model.Plan;

public class MainAffairPlanAdapter extends BaseAdapter {
	private Context context;
	List<Plan> data;

	public MainAffairPlanAdapter(Context context,List<Plan> datalist) {
		this.context = context;
		this.data = datalist;
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
	public View getView(int idx, View convertView, ViewGroup parent) {
		View row = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.main_affair_plan_item, parent, false);
		} else {
			row = convertView;
		}
		Plan plan = data.get(idx);
		TextView  thingname = (TextView) row.findViewById(R.id.affair_plan_item_left_thingname);
		thingname.setText(plan.getTitle());
		TextView time = (TextView) row.findViewById(R.id.affair_plan_item_mid_top);
		time.setText(plan.getCreateTime()+"");
		TextView targettext = (TextView) row.findViewById(R.id.affair_plan_item_mid_bottom);
		targettext.setText(plan.getTitle());
		return row;
	}

}
