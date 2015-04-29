package com.david.pda.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.david.pda.R;
import com.david.pda.sqlite.model.Principle;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.weather.model.util.L;

public class SelfPrincipleListAdapter extends BaseAdapter {
	private Context context;
	private List<Principle> data;

	public SelfPrincipleListAdapter(Context context) {
		this.context = context;
		this.data = new DemoDB<Principle>(new Principle()).getList(context);
		Log.i(L.t, data.size() + "");
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
			row = inflater.inflate(R.layout.list_item_self_principle, parent,
					false);
		} else {
			row = convertView;
		}
		TextView title = (TextView) row
				.findViewById(R.id.list_item_self_principle_title);
		Principle ct = data.get(position);
		title.setText(ct.getTitle());
		return row;
	}
}
