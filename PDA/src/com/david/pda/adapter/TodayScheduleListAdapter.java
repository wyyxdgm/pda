package com.david.pda.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.david.pda.R;
import com.david.pda.sqlite.model.CycleDetailsForPlan;
import com.david.pda.sqlite.model.Plan;
import com.david.pda.util.other.DateUtil;
import com.david.pda.util.other.DrawUtil;

public class TodayScheduleListAdapter extends BaseAdapter {
	Map<Long, Plan> planMap;
	List<CycleDetailsForPlan> ds;
	Context context;

	public TodayScheduleListAdapter(Map<Long, Plan> planMap,
			List<CycleDetailsForPlan> ds, Context context) {
		this.planMap = planMap;
		this.ds = ds;
		this.context = context;
	}

	@Override
	public int getCount() {
		return ds.size();
	}

	@Override
	public Object getItem(int arg0) {
		return ds.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		View row = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.list_item_today_schedule, parent,
					false);
		} else {
			row = convertView;
		}
		CycleDetailsForPlan cs = ds.get(i);
		Plan plan = planMap.get(cs.getCycleFor().longValue());
		TextView content = (TextView) row
				.findViewById(R.id.list_item_today_schedule_content);
		TextView title = (TextView) row
				.findViewById(R.id.list_item_today_schedule_title);
		CheckBox jj = (CheckBox) row
				.findViewById(R.id.list_item_today_schedule_jj);
		CheckBox zy = (CheckBox) row
				.findViewById(R.id.list_item_today_schedule_zy);
		TextView time = (TextView) row
				.findViewById(R.id.list_item_today_schedule_time);
		ImageView imageView = (ImageView) row
				.findViewById(R.id.list_item_today_schedule_scale_image);
		// set to view
		time.setText("时间：" + DateUtil.format("HH:mm:ss", cs.getStartTime())
				+ "-" + DateUtil.format("HH:mm:ss", cs.getEndTime()));
		jj.setChecked(plan.getUrgencyimportant() == null ? false : plan
				.getUrgencyimportant() >> 1 == 1);
		zy.setChecked(plan.getUrgencyimportant() == null ? false : plan
				.getUrgencyimportant() % 2 == 1);
		content.setText(cs.getDiscription());
		title.setText(plan.getTitle());
		Canvas c;
		Paint p;
		RectF rectf = new RectF(0, 0, 80, 80);// 380/2=190
		Bitmap currentBitmap;
		currentBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.s80).copy(Bitmap.Config.ARGB_8888, true);
		imageView.setScaleType(ScaleType.CENTER);
		c = new Canvas(currentBitmap);
		int scale = (int) ((cs.getStartTime() - System.currentTimeMillis()) * 360.0 / (DateUtil
				.getTodayEndTime() - System.currentTimeMillis()));
		p = DrawUtil.getPaintByAndgle(scale);
		c.drawArc(rectf, 180, scale, true, p);
		p.setStyle(Paint.Style.STROKE);
		c.drawArc(rectf, 180 + scale, 360 - scale, true, p);
		imageView.setImageBitmap(currentBitmap);
		return row;
	}
}
