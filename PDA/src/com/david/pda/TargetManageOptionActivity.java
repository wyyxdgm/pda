package com.david.pda;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.david.pda.sqlite.model.Target;
import com.david.pda.util.other.Bind;
import com.david.pda.util.other.DrawUtil;
import com.david.pda.weather.model.util.L;

public class TargetManageOptionActivity extends Activity {
	ImageView imageView;
	ImageButton backward;
	Canvas c;
	Paint p;
	RectF rectf = new RectF(10, 10, 390, 390);// 380/2=190
	Bitmap currentBitmap;
	private List<Target> targets = new ArrayList<Target>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.target_manage_option);
		backward = (ImageButton) findViewById(R.id.main_target_manage_option_topbar_backward);
		imageView = (ImageView) findViewById(R.id.main_target_manage_option_imageView);
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
		Display display = getWindowManager().getDefaultDisplay(); // Activity#getWindowManager()
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Log.i(L.t, "width:" + size.x);
		Log.i(L.t, "height:" + size.y);
		Log.i(L.t, "center----:[" + size.x / 2 + "," + size.y / 2 + "]");
		Log.i(L.t, "leftEdge:" + (size.x / 2 - 190));
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

			case MotionEvent.ACTION_DOWN: {
				Log.i(L.mo, "ACTION_DOWN" + text);
				// 按下时触发
			}
				break;
			case MotionEvent.ACTION_MOVE: {
				// 移动时触发
				Log.i(L.mo, "ACTION_MOVE" + text);
			}
				break;
			case MotionEvent.ACTION_UP: {
				// 触摸后触发
				Log.i(L.mo, "ACTION_UP" + text);
			}
				break;
			}
			return true;
		}
	}

	public int getBlockIndexByXY(int x, int y) {

		return 0;
	}

	public void loadData() {
		if (targets == null) {
			targets = new ArrayList<Target>();
		}
		targets.add(new Target("english", 200));
		targets.add(new Target("computer", 100));
		targets.add(new Target("math", 60));
	}

	public void drawDataToBitmap() {
		for (int i = 0, startScale = 0; i < targets.size(); i++) {
			Target t = targets.get(i);
			Paint p = DrawUtil.getPaint(i, targets.size() - 1 == i);
			c.drawArc(rectf, startScale, startScale + t.getScale(), true, p);
			startScale += t.getScale();
		}
	}
}
// c.setBitmap(currentBitmap);
// c.drawArc(rectf, RandUtil.getInt(360), RandUtil.getInt(10),
// true, p);
// imageView.setImageBitmap(currentBitmap);
