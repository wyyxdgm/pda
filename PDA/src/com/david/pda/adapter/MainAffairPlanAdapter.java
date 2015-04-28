package com.david.pda.adapter;

import java.util.List;

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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.david.pda.R;
import com.david.pda.sqlite.model.Plan;
import com.david.pda.util.other.DateUtil;

public class MainAffairPlanAdapter extends BaseAdapter {
	private Context context;
	List<Plan> data;

	public MainAffairPlanAdapter(Context context, List<Plan> datalist) {
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
			row = inflater.inflate(R.layout.main_affair_plan_item, parent,
					false);
		} else {
			row = convertView;
		}
		Plan plan = data.get(idx);
		TextView thingname = (TextView) row
				.findViewById(R.id.affair_plan_item_left_thingname);
		thingname.setText("标题：" + plan.getTitle());
		TextView time = (TextView) row
				.findViewById(R.id.affair_plan_item_mid_top);
		time.setText("开始时间："
				+ DateUtil.formatyyyy_MM_dd_HH_mm(plan.getStartTime()));
		TextView targettext = (TextView) row
				.findViewById(R.id.affair_plan_item_mid_bottom);
		targettext.setText("结束时间："
				+ DateUtil.formatyyyy_MM_dd_HH_mm(plan.getEndTime()));
		ImageView imageView = (ImageView) row
				.findViewById(R.id.affair_plan_scale_image);
		Canvas c;
		Paint p;
		RectF rectf = new RectF(0, 0, 80, 80);// 380/2=190
		Bitmap currentBitmap;
		currentBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.s80).copy(Bitmap.Config.ARGB_8888, true);
		imageView.setScaleType(ScaleType.CENTER);
		c = new Canvas(currentBitmap);
		int scale = (int) ((plan.getEndTime() - System.currentTimeMillis()) * 360.0 / (plan
				.getEndTime() - plan.getStartTime()));
		p = DateUtil.getPaint(scale);
		c.drawArc(rectf, 180, scale, true, p);
		p.setStyle(Paint.Style.STROKE);
		c.drawArc(rectf, 180 + scale, 360 - scale, true, p);
		imageView.setImageBitmap(currentBitmap);
		return row;
	}
}
