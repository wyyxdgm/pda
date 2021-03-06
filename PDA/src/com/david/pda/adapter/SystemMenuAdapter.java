
package com.david.pda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.pda.R;

public class SystemMenuAdapter extends BaseAdapter {

	private Context context;
	String[] systemMenusStr;
	int[] images = { R.drawable.menu_target_manage, R.drawable.menu_affair_plan,
			R.drawable.menu_today_shedule, R.drawable.menu_four_classes,
			R.drawable.menu_some_tools, R.drawable.menu_system_settting/*,
			R.drawable.ic_resortandbeaches */};

	public SystemMenuAdapter(Context context) {
		this.context = context;
		systemMenusStr = context.getResources().getStringArray(R.array.system_menu);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return systemMenusStr.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return systemMenusStr[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = null;
		if (convertView == null) {
			// create the row for the first time
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.custom_row, parent, false);
		} else {
			row = convertView;
		}
		TextView titleTextView = (TextView) row.findViewById(R.id.textView1);
		ImageView imageView = (ImageView) row.findViewById(R.id.imageView1);
		titleTextView.setText(systemMenusStr[position]);
		imageView.setImageResource(images[position]);
		return row;
	}

}
