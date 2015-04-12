package com.david.pda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.pda.R;

public class BataanAdapter extends BaseAdapter {

	private Context context;
	String[] bataan;
	int[] images = { R.drawable.ic_home, R.drawable.ic_events,
			R.drawable.ic_festival, R.drawable.ic_landmark,
			R.drawable.ic_hotel, R.drawable.ic_resto,
			R.drawable.ic_resortandbeaches };

	public BataanAdapter(Context context) {
		this.context = context;
		bataan = context.getResources().getStringArray(R.array.bataan);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bataan.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bataan[position];
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
		titleTextView.setText(bataan[position]);
		imageView.setImageResource(images[position]);
		return row;
	}

}
