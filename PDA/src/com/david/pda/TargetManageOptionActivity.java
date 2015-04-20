package com.david.pda;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.david.pda.sqlite.model.Target;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;
import com.david.pda.util.other.DrawUtil;
import com.david.pda.weather.model.util.L;

public class TargetManageOptionActivity extends Activity {
	ImageView imageView;
	ImageButton backward;
	GridView gridView;
	LinearLayout linearLayout;
	RelativeLayout topBar;
	Canvas c;
	Paint p;
	RectF rectf = new RectF(0, 0, 400, 400);// 380/2=190
	Bitmap currentBitmap;
	private List<Target> targets = new ArrayList<Target>();
	private Target startTarget;
	private Target endTarget;
	private long[] times = new long[] { 1l, 2l };
	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_target_manage_option);
		backward = (ImageButton) findViewById(R.id.main_target_manage_option_topbar_backward);
		imageView = (ImageView) findViewById(R.id.main_target_manage_option_imageView);
		gridView = (GridView) findViewById(R.id.main_target_manage_option_gridView);
		topBar = (RelativeLayout) findViewById(R.id.main_target_manage_option_topbar);
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
				times[0] = System.currentTimeMillis();
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
				times[1] = System.currentTimeMillis();
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
			if (startTarget.getName().equals(endTarget.getName())) {
				long gap = times[1] - times[0];
				if (gap > 1500) {
					showWindowForAdd();
				} else {
					Toast.makeText(this,
							"想要通过'" + endTarget.getName() + "'分身？请长按",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				// Toast.makeText(this,
				// startTarget.getName() + "->" + endTarget.getName(),
				// Toast.LENGTH_SHORT).show();
				showWindowForUpdate();
			}
		}
		// 不管如何，都清零
		startTarget = null;
		endTarget = null;
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
			cc.drawCircle(20, 20, 10, p);
			ImageView imageView = new ImageView(this);
			imageView.setImageBitmap(bm);
			imageView.setScaleType(ScaleType.CENTER);
			imageView.setMaxHeight(10);
			imageView.setMaxWidth(10);
			TextView txt = new TextView(this);
			txt.setText(targets.get(i).getName());
			l.addView(imageView);
			l.addView(txt);
			linearLayout.addView(l);
		}
	}

	private void showWindowForAdd() {
		if (startTarget == null) {
			return;
		}
		initWindow();
		popupAddLayout.setVisibility(View.VISIBLE);
		popupUpdateLayout.setVisibility(View.INVISIBLE);
		popupAddTitle.setText("adddddddddd");
		popupAddTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideWindow();
			}
		});
		initAddLayoutContent();
		showWindow();
	}

	private void showWindowForUpdate() {
		if (startTarget == null || endTarget == null) {
			return;
		}
		initWindow();
		popupAddLayout.setVisibility(View.INVISIBLE);
		popupUpdateLayout.setVisibility(View.VISIBLE);
		popupUpdateTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideWindow();
			}
		});
		initUpdateLayoutContent();
		showWindow();
	}

	private void initWindow() {
		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			popupView = layoutInflater.inflate(
					R.layout.main_target_manage_option_popup, null);
			// 创建一个PopuWidow对象
			popupUpdateLayout = (RelativeLayout) popupView
					.findViewById(R.id.main_target_manage_option_popup_update_layout);
			popupAddLayout = (RelativeLayout) popupView
					.findViewById(R.id.main_target_manage_option_popup_add_layout);
			popupWindow = new PopupWindow(popupView, 300, 350);
			initLayout();
			popupAddTitle.setText("添加分身");
			popupUpdateTitle.setText("分身传输");
		}
	}

	private void showWindow() {
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		int xPos = size.x / 2 - popupWindow.getWidth() / 2;
		popupWindow.showAsDropDown(topBar, xPos, 20);
	}

	private void hideWindow() {
		if (popupWindow != null) {
			popupWindow.dismiss();
		}
	}

	private void initUpdateLayoutContent() {
		popupUpdateStartTargetName.setText(startTarget.getName());
		popupUpdateEndTargetName.setText(endTarget.getName());
		popupUpdateSeekbar
				.setMax(startTarget.getScale() + endTarget.getScale());
		popupUpdateSeekbar.setProgress(startTarget.getScale());
		popupUpdateSeekbarScaleLeft.setText(startTarget.getScale());
		popupUpdateSeekbarScaleRight.setText(endTarget.getScale());
		popupUpdateSeekbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar arg0) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						popupUpdateSeekbarScaleLeft.setText(seekBar
								.getProgress());
						popupUpdateSeekbarScaleRight.setText(seekBar.getMax()
								- seekBar.getProgress());
					}

					@Override
					public void onProgressChanged(SeekBar arg0, int arg1,
							boolean arg2) {

					}
				});
		popupUpdateButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideWindow();
			}
		});
		popupUpdateButtonConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DemoDB<Target> db = new DemoDB<Target>(startTarget);
				startTarget.setScale(popupUpdateSeekbar.getProgress());
				endTarget.setScale(popupUpdateSeekbar.getMax()
						- popupUpdateSeekbar.getProgress());
				try {// 修改后，如果比例为零，表示要删掉
					if (startTarget.getScale() > 0) {
						db.update(startTarget, TargetManageOptionActivity.this);
					} else {
						db.remove(startTarget.get_id() + "",
								TargetManageOptionActivity.this);
					}
					if (endTarget.getScale() > 0) {
						db.update(endTarget, TargetManageOptionActivity.this);
					} else {
						db.remove(endTarget.get_id() + "",
								TargetManageOptionActivity.this);
					}
					Toast.makeText(TargetManageOptionActivity.this, "操作成功！",
							Toast.LENGTH_SHORT).show();
					hideWindow();
					loadData();
					drawDataToBitmap();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// add layout

	private void initAddLayoutContent() {
		popupAddStartTargetName.setText(startTarget.getName());
		popupAddEndTargetName.setText("请再次输入分身名称");
		popupAddSeekbar.setMax(startTarget.getScale());
		popupAddSeekbar.setProgress(startTarget.getScale());
		popupAddSeekbarScaleLeft.setText(startTarget.getScale());
		popupAddSeekbarScaleRight.setText(0);
		popupAddSeekbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar arg0) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						popupAddSeekbarScaleLeft.setText(seekBar.getProgress());
						popupAddSeekbarScaleRight.setText(seekBar.getMax()
								- seekBar.getProgress());
					}

					@Override
					public void onProgressChanged(SeekBar arg0, int arg1,
							boolean arg2) {

					}
				});
		popupAddButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideWindow();
			}
		});
		popupAddButtonConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DemoDB<Target> db = new DemoDB<Target>(startTarget);
				startTarget.setScale(popupAddSeekbar.getProgress());
				endTarget.setScale(popupAddSeekbar.getMax()
						- popupAddSeekbar.getProgress());
				endTarget.setDelFlag(Model.FLAG_EXISTS);
				endTarget.setName(popupAddEndTargetName.getText().toString());
				if (TextUtils.isEmpty(endTarget.getName())) {
					Toast.makeText(TargetManageOptionActivity.this,
							"分身名称不能为空，请重新填写！", Toast.LENGTH_SHORT).show();
					return;
				}
				try {// 修改后，如果比例为零，表示要删掉
					if (startTarget.getScale() > 0) {
						db.update(startTarget, TargetManageOptionActivity.this);
					} else {
						db.remove(startTarget.get_id() + "",
								TargetManageOptionActivity.this);
					}
					if (endTarget.getScale() > 0) {
						db.insert(endTarget, TargetManageOptionActivity.this);
					}
					Toast.makeText(TargetManageOptionActivity.this, "操作成功！",
							Toast.LENGTH_SHORT).show();
					hideWindow();
					loadData();
					drawDataToBitmap();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// window
	private View popupView;
	// update
	private RelativeLayout popupUpdateLayout;
	private TextView popupUpdateTitle;// main_target_manage_option_popup_update_title
	private TextView popupUpdateStartTargetName;// main_target_manage_option_popup_update_start_target
	private TextView popupUpdateEndTargetName;// main_target_manage_option_popup_update_end_target
	private TextView popupUpdateSeekbarScaleLeft;// main_target_manage_option_popup_update_seekbar_left
	private TextView popupUpdateSeekbarScaleRight;// main_target_manage_option_popup_update_seekbar_right
	private SeekBar popupUpdateSeekbar;// main_target_manage_option_popup_update_seekbar
	private Button popupUpdateButtonCancel;// main_target_manage_option_popup_update_button_cancel
	private Button popupUpdateButtonConfirm;// addmain_target_manage_option_popup_update_button_confirm

	private RelativeLayout popupAddLayout;
	private TextView popupAddTitle;// main_target_manage_option_popup_add_title
	private TextView popupAddStartTargetName;// main_target_manage_option_popup_add_start_target
	private TextView popupAddEndTargetName;// main_target_manage_option_popup_add_end_target
	private TextView popupAddSeekbarScaleLeft;// main_target_manage_option_popup_add_seekbar_left
	private TextView popupAddSeekbarScaleRight;// main_target_manage_option_popup_add_seekbar_right
	private SeekBar popupAddSeekbar;// main_target_manage_option_popup_add_seekbar
	private Button popupAddButtonCancel;// main_target_manage_option_popup_add_button_cancel
	private Button popupAddButtonConfirm;// addmain_target_manage_option_popup_add_button_confirm

	private void initLayout() {
		// update

		popupUpdateTitle = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_title);
		popupUpdateStartTargetName = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_start_target);// main_target_manage_option_popup_update_start_target
		popupUpdateEndTargetName = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_end_target);// main_target_manage_option_popup_update_end_target
		popupUpdateSeekbarScaleLeft = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_seekbar_left);// main_target_manage_option_popup_update_seekbar_left
		popupUpdateSeekbarScaleRight = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_seekbar_right);// main_target_manage_option_popup_update_seekbar_right
		popupUpdateSeekbar = (SeekBar) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_seekbar);// main_target_manage_option_popup_update_seekbar
		popupUpdateButtonCancel = (Button) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_button_cancel);// main_target_manage_option_popup_update_button_cancel
		popupUpdateButtonConfirm = (Button) popupView
				.findViewById(R.id.main_target_manage_option_popup_update_button_confirm);// addmain_target_manage_option_popup_update_button_confirm
		// add
		popupAddTitle = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_title);
		popupAddStartTargetName = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_start_target);// main_target_manage_option_popup_add_start_target
		popupAddEndTargetName = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_end_target);// main_target_manage_option_popup_add_end_target
		popupAddSeekbarScaleLeft = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_seekbar_left);// main_target_manage_option_popup_add_seekbar_left
		popupAddSeekbarScaleRight = (TextView) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_seekbar_right);// main_target_manage_option_popup_add_seekbar_right
		popupAddSeekbar = (SeekBar) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_seekbar);// main_target_manage_option_popup_add_seekbar
		popupAddButtonCancel = (Button) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_button_cancel);// main_target_manage_option_popup_add_button_cancel
		popupAddButtonConfirm = (Button) popupView
				.findViewById(R.id.main_target_manage_option_popup_add_button_confirm);// addmain_target_manage_option_popup_add_button_confirm
	}
	//
}
