package com.david.pda.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.david.pda.R;
import com.david.pda.SomeToolsMemoActivity;
import com.david.pda.sqlite.model.Memo;
import com.david.pda.util.other.DateUtil;

public class MemoGridAdapter extends BaseAdapter {
	private Context context;
	List<Memo> data;

	public MemoGridAdapter(Context context) {
		this.context = context;
		this.data = ((SomeToolsMemoActivity) context).getData();
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
			row = inflater.inflate(R.layout.grid_item_memo, parent, false);
		} else {
			row = convertView;
		}
		TextView contentView = (TextView) row
				.findViewById(R.id.some_tools_memo_item_content);
		TextView titleView = (TextView) row
				.findViewById(R.id.some_tools_memo_item_title);
		TextView createTimeView = (TextView) row
				.findViewById(R.id.some_tools_memo_item_createTime);
		CheckedTextView flagView = (CheckedTextView) row
				.findViewById(R.id.some_tools_memo_item_flag);
		Memo memo = data.get(position);
		if (memo.getTitle() != null && memo.getTitle().length() > 8) {
			titleView.setText(memo.getTitle().subSequence(0, 6) + "...");
		} else {
			titleView.setText(memo.getTitle());
		}
		if (memo.getContent() != null && memo.getContent().length() > 25) {
			contentView.setText(memo.getContent().subSequence(0, 25) + "...");
		} else {
			contentView.setText(memo.getContent());
		}
		createTimeView
				.setText(DateUtil.formatMM_dd_HH_mm(memo.getCreateTime()));
		if (memo.getFlag() == 1) {
			flagView.setCheckMarkDrawable(R.drawable.flag_mark_red);
			flagView.setVisibility(View.VISIBLE);
		} else {
			flagView.setVisibility(View.INVISIBLE);
		}
		return row;
	}
}
