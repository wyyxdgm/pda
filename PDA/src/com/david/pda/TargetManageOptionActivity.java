package com.david.pda;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.david.pda.sqlite.model.Target;
import com.david.pda.util.other.Bind;
import com.david.pda.util.other.DrawUtil;
import com.david.pda.weather.model.util.L;

public class TargetManageOptionActivity extends Activity {
	ImageView imageView;
	ImageButton backward;
	GridView gridView;
	LinearLayout linearLayout;
	Canvas c;
	Paint p;
	RectF rectf = new RectF(0, 0, 400, 400);// 380/2=190
	Bitmap currentBitmap;
	private List<Target> targets = new ArrayList<Target>();
	private Target startTarget;
	private Target endTarget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.target_manage_option);
		backward = (ImageButton) findViewById(R.id.main_target_manage_option_topbar_backward);
		imageView = (ImageView) findViewById(R.id.main_target_manage_option_imageView);
		gridView = (GridView) findViewById(R.id.main_target_manage_option_gridView);
		linearLayout = (LinearLayout) findViewById(R.id.main_target_manage_option_linearLayout);
		Bind.bindReturn(backward, TargetManageOptionActivity.this,
				MainActivity.class, MainActivity.POSTION_TARGET_MANAGE);
		currentBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.s400).copy(Bitmap.Config.ARGB_8888, true);
		c = new Canvas(currentBitmap);
		Log.i(L.t, "left" + imageView.getPivotX());
		Log.i(L.t, "right" + imageView.getPivotX() + imageView.getWidth());
		Log.i(L.t, "top" + imageView.getPivotY());
		Log.i(L.t, "bottom" + imageView.getScaleY() + imageView.getHeight());
		Log.i(L.t,
				"center:" + "["
						+ (imageView.getScaleX() + imageView.getWidth() / 2)
						+ ","
						+ (imageView.getScaleY() + imageView.getHeight() / 2)
						+ "]");
		// Display display = getWindowManager().getDefaultDisplay(); //
		// Activity#getWindowManager()
		// Point size = new Point();
		// display.getSize(size);
		// Log.i(L.t, "width:" + size.x);
		// Log.i(L.t, "height:" + size.y);
		// Log.i(L.t, "center----:[" + size.x / 2 + "," + size.y / 2 + "]");
		// Log.i(L.t, "leftEdge:" + (size.x / 2 - 190));
		loadData();
		drawDataToBitmap();// 利用数据 draw bitmap
		imageView.setImageBitmap(currentBitmap);
		imageView.setOnTouchListener(new ImageOnTouchListener());
	}

	class ImageOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent e) {
			String text = e.getX() + "," + e.getY();

			// Toast.makeText(TargetManageOptionActivity.this, text,
			// Toast.LENGTH_SHORT).show();

			switch (e.getAction()) {

			case MotionEvent.ACTION_DOWN: {// 按下时触发
				Log.i(L.mo, "ACTION_DOWN" + text);
				startTarget = getBlockIndexByXY(e.getX(), e.getY());
				if (startTarget != null)
					Log.i(L.mo, "startTarget:" + startTarget.getName());
			}
				break;
			case MotionEvent.ACTION_MOVE: {// 移动时触发
			}
				break;
			case MotionEvent.ACTION_UP: {
				// 触摸后触发
				Log.i(L.mo, "ACTION_UP" + text);
				endTarget = getBlockIndexByXY(e.getX(), e.getY());
				if (endTarget != null)
					Log.i(L.mo, "endTarget:" + endTarget.getName());

				doAfterActionUp();
			}
				break;
			}
			return true;
		}
	}

	private void doAfterActionUp() {
		if (startTarget != null && endTarget != null) {
			Toast.makeText(this,
					startTarget.getName() + "->" + endTarget.getName(),
					Toast.LENGTH_SHORT).show();
			startTarget = null;
			endTarget = null;
		}
	}

	private Target getBlockIndexByXY(float x, float y) {
		return getTargetByScale(getScaleByXY(x, y));
	}

	private Target getTargetByScale(int scale) {
		for (int i = 0, startScale = 0; i < targets.size(); i++) {
			Target t = targets.get(i);
			if (scale > startScale && scale <= startScale + t.getScale()) {
				return targets.get(i);
			}
			startScale += t.getScale();
		}
		return null;
	}

	/*
	 * COS B = (a2+c2-b2)/2ab COSA=(c^2+b^2-a^2)/2bc坐由反余弦得出：
	 * 标A=ACOS(COSA)*180/PI()
	 */
	private int getScaleByXY(float x, float y) {
		if (isInCircle(x, y)) {
			Log.i(L.mo, "y");
			if (x == 200) {
				if (y > 200) {
					return 270;
				} else if (y < 200) {
					return 90;
				} else {// y==0
					return -1;
				}
			}
			if (y == 200) {
				if (x < 200) {
					return 180;
				} else if (x > 200) {
					return 0;
				} else
					return -1;
			}
			double scalePi = Math.atan2(200 - y, x - 200);
			Log.i(L.mo, "scalePi:" + scalePi);
			return (int) (180 - scalePi / Math.PI * 180);
		} else {
			Log.i(L.mo, "n");
		}
		return -1;
	}

	private boolean isInCircle(float x, float y) {
		return Math.pow(x - 200, 2) + Math.pow(y - 200, 2) < Math.pow(200, 2);
	}

	private void loadData() {
		if (targets == null) {
			targets = new ArrayList<Target>();
		}
		targets.add(new Target("english", 200));
		targets.add(new Target("computer", 100));
		targets.add(new Target("math", 60));
	}

	private void drawDataToBitmap() {
		Canvas cc = new Canvas();
		for (int i = 0, startScale = 0; i < targets.size(); i++) {
			Target t = targets.get(i);
			Paint p = DrawUtil.getPaint(i, targets.size() - 1 == i);
			c.drawArc(rectf, startScale, startScale + t.getScale(), true, p);
			startScale += t.getScale();

			LinearLayout l = new LinearLayout(this);
			l.setOrientation(LinearLayout.HORIZONTAL);
			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.s40).copy(Bitmap.Config.ARGB_8888, true);
			cc.setBitmap(bm);
			cc.drawCircle(20, 20, 20, p);

			ImageView imageView = new ImageView(this);
			imageView.setImageBitmap(bm);
			imageView.setMaxHeight(10);
			imageView.setMaxWidth(10);

			TextView txt = new TextView(this);
			txt.setText(targets.get(i).getName());

			l.addView(imageView);
			l.addView(txt);
			gridView.addView(l);
		}
	}
}
// c.setBitmap(currentBitmap);
// c.drawArc(rectf, RandUtil.getInt(360), RandUtil.getInt(10),
// true, p);
// imageView.setImageBitmap(currentBitmap);
